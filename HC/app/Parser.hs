module Parser where

import Syntax
import System.IO
import Control.Monad
import Text.ParserCombinators.Parsec
import Text.ParserCombinators.Parsec.Expr
import Text.ParserCombinators.Parsec.Language
import qualified Text.ParserCombinators.Parsec.Token as Token

languageDef =
    emptyDef { Token.commentStart    = "/*"
             , Token.commentEnd      = "*/"
             , Token.commentLine     = "//"
             , Token.identStart      = letter
             , Token.identLetter     = alphaNum
             , Token.reservedNames   = [ "if"
                                       , "then"
                                       , "else"
                                       , "while"
                                       , "do"
                                       , "end"
                                       , "true"
                                       , "false"
                                       , "def"
                                       , "in"
                                       ]
             , Token.reservedOpNames = ["+", "-", "*", "/", ":="
                                       , "==", ">", "&&", "||", "~", "!", "new", "print"
                                       ]
             }


lexer = Token.makeTokenParser languageDef

identifier = Token.identifier lexer -- parses an identifier
reserved   = Token.reserved   lexer -- parses a reserved name
reservedOp = Token.reservedOp lexer -- parses an operator
parens     = Token.parens     lexer -- parses surrounding parenthesis:
                                    --   parens p
                                    -- takes care of the parenthesis and
                                    -- uses p to parse what's inside them
integer    = Token.integer    lexer -- parses an integer
semi       = Token.semi       lexer -- parses a semicolon
whiteSpace = Token.whiteSpace lexer -- parses whitespace


newtype ParseExpr = PE (Expr a)
                 deriving (Show)

operators :: [[Operator Char st (ParseExpr a)]]
operators = [
        [Prefix (reservedOp "-" >> return (\(PE x) -> PE $ UMinus x)),
         Prefix (reservedOp "~" >> return (\(PE x) -> PE $ LogNeg x)),
         Prefix (reservedOp "new" >> return (\(PE x) -> PE $ New x)),
         Prefix (reservedOp "print" >> return (\(PE x) -> PE $ Print x)),
         Prefix (reservedOp "!" >> return (\(PE x) -> PE $ Deref x))
        ],

        [Infix  (reservedOp "*"   >> return (\(PE x) (PE y) -> PE $ Mul x y)) AssocLeft],

        [Infix  (reservedOp "+"   >> return (\(PE x) (PE y) -> PE $ Add x y)) AssocLeft],

        [Infix  (reservedOp "=="   >> return (\(PE x) (PE y) -> PE $ RelEq x y)) AssocLeft],

        [Infix  (reservedOp "&&"   >> return (\(PE x) (PE y) -> PE $ LogAnd x y)) AssocLeft],

        [Infix  (reservedOp "||"   >> return (\(PE x) (PE y) -> PE $ LogOr x y)) AssocLeft],

        [Infix  (reservedOp ":="   >> return (\(PE x) (PE y) -> PE $ Assign x y)) AssocLeft],

        [Infix  (reservedOp ";"   >> return (\(PE x) (PE y) -> PE $ Seq x y)) AssocLeft]
    ]

parseExpr :: Parser (ParseExpr a)
parseExpr = whiteSpace >> expression

expression :: Parser (ParseExpr a)
expression = buildExpressionParser operators primaryExpr

primaryExpr :: Parser (ParseExpr a)
primaryExpr = parens expression
            <|> PE . Id <$> identifier
            <|> PE . LitNum <$> integer
            <|> (reserved "true" >> return (PE $ LitBool True))
            <|> (reserved "false" >> return (PE $ LitBool False))
            <|> definitionExpr
            <|> selectionExpr
            <|> iterationExpr

definitionExpr :: Parser (ParseExpr a)
definitionExpr = do
    reserved "def"
    la <- many1 (do
            x <- identifier
            reservedOp "="
            PE y <- expression
            return (x, y)
        )
    reserved "in"
    PE b <- expression
    reserved "end"
    return $ PE $ Def la b

selectionExpr :: Parser (ParseExpr a)
selectionExpr = do
    reserved "if"
    PE cond <- expression
    reserved "then"
    PE a <- expression
    reserved "else"
    PE b <- expression
    reserved "end"
    return $ PE $ If cond a b

iterationExpr :: Parser (ParseExpr a)
iterationExpr = do
    reserved "while"
    PE cond <- expression
    reserved "do"
    PE bod <- expression
    reserved "end"
    return $ PE $ While cond bod

