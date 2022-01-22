{-# LANGUAGE ScopedTypeVariables #-}
module Parser where

import System.IO
import Control.Monad
import Text.ParserCombinators.Parsec
import Text.ParserCombinators.Parsec.Expr
import Text.ParserCombinators.Parsec.Language
import qualified Text.ParserCombinators.Parsec.Token as Token
import Data.Bifunctor

data UExpr
    = ULitNum Integer
    | ULitBool Bool
    | UId String
    | UAdd UExpr UExpr
    | UMul UExpr UExpr
    | UUMinus UExpr
    | ULogNeg UExpr
    | URelEq UExpr UExpr
    | ULogOr UExpr UExpr
    | ULogAnd UExpr UExpr
    | UDef [(String, UExpr)] UExpr
    | UNew UExpr
    | UIf UExpr UExpr UExpr
    | UWhile UExpr UExpr
    | UPrint UExpr
    | USeq UExpr UExpr
    | UAssign UExpr UExpr
    | UDeref UExpr

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


operators :: [[Operator Char st UExpr]]
operators = [
        [Prefix (reservedOp "-" >> return UUMinus),
         Prefix (reservedOp "~" >> return ULogNeg),
         Prefix (reservedOp "new" >> return UNew),
         Prefix (reservedOp "print" >> return UPrint),
         Prefix (reservedOp "!" >> return UDeref)
        ],

        [Infix  (reservedOp "*"   >> return UMul) AssocLeft],

        [Infix  (reservedOp "+"   >> return UAdd) AssocLeft],

        [Infix  (reservedOp "=="   >> return URelEq) AssocLeft],

        [Infix  (reservedOp "&&"   >> return ULogAnd) AssocLeft],

        [Infix  (reservedOp "||"   >> return ULogOr) AssocLeft],

        [Infix  (reservedOp ":="   >> return UAssign) AssocLeft],

        [Infix  (reservedOp ";"   >> return USeq) AssocLeft]
    ]

parseExpr :: Parser UExpr
parseExpr = whiteSpace >> expression

expression :: Parser UExpr
expression = buildExpressionParser operators primaryExpr

primaryExpr :: Parser UExpr
primaryExpr = parens expression
            <|> UId <$> identifier
            <|> ULitNum <$> integer
            <|> (reserved "true" >> return (ULitBool True))
            <|> (reserved "false" >> return (ULitBool False))
            <|> definitionExpr
            <|> selectionExpr
            <|> iterationExpr

definitionExpr :: Parser UExpr
definitionExpr = do
    reserved "def"
    la <- many1 (do
            x <- identifier
            reservedOp "="
            y <- expression
            return (x, y)
        )
    reserved "in"
    b <- expression
    reserved "end"
    return $ UDef la b

selectionExpr :: Parser UExpr
selectionExpr = do
    reserved "if"
    cond <- expression
    reserved "then"
    a <- expression
    reserved "else"
    b <- expression
    reserved "end"
    return $ UIf cond a b

iterationExpr :: Parser UExpr
iterationExpr = do
    reserved "while"
    cond <- expression
    reserved "do"
    bod <- expression
    reserved "end"
    return $ UWhile cond bod

