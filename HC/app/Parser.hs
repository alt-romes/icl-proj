{-# LANGUAGE TypeApplications #-}
{-# LANGUAGE ScopedTypeVariables #-}
module Parser where

import qualified Syntax as S
import System.IO
import Control.Monad
import Text.ParserCombinators.Parsec
import Text.ParserCombinators.Parsec.Expr
import Text.ParserCombinators.Parsec.Language
import qualified Text.ParserCombinators.Parsec.Token as Token
import Data.Bifunctor

data ParseExpr
    = LitNum Integer
    | LitBool Bool
    | Id String
    | Add ParseExpr ParseExpr
    | Mul ParseExpr ParseExpr
    | UMinus ParseExpr
    | LogNeg ParseExpr
    | RelEq ParseExpr ParseExpr
    | LogOr ParseExpr ParseExpr
    | LogAnd ParseExpr ParseExpr
    | Def [(String, ParseExpr)] ParseExpr
    | New ParseExpr
    | If ParseExpr ParseExpr ParseExpr
    | While ParseExpr ParseExpr
    | Print ParseExpr
    | Seq ParseExpr ParseExpr
    | Assign ParseExpr ParseExpr
    | Deref ParseExpr

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


operators :: [[Operator Char st ParseExpr]]
operators = [
        [Prefix (reservedOp "-" >> return UMinus),
         Prefix (reservedOp "~" >> return LogNeg),
         Prefix (reservedOp "new" >> return New),
         Prefix (reservedOp "print" >> return Print),
         Prefix (reservedOp "!" >> return Deref)
        ],

        [Infix  (reservedOp "*"   >> return Mul) AssocLeft],

        [Infix  (reservedOp "+"   >> return Add) AssocLeft],

        [Infix  (reservedOp "=="   >> return RelEq) AssocLeft],

        [Infix  (reservedOp "&&"   >> return LogAnd) AssocLeft],

        [Infix  (reservedOp "||"   >> return LogOr) AssocLeft],

        [Infix  (reservedOp ":="   >> return Assign) AssocLeft],

        [Infix  (reservedOp ";"   >> return Seq) AssocLeft]
    ]

parseExpr :: Parser ParseExpr
parseExpr = whiteSpace >> expression

expression :: Parser ParseExpr
expression = buildExpressionParser operators primaryExpr

primaryExpr :: Parser ParseExpr
primaryExpr = parens expression
            <|> Id <$> identifier
            <|> LitNum <$> integer
            <|> (reserved "true" >> return (LitBool True))
            <|> (reserved "false" >> return (LitBool False))
            <|> definitionExpr
            <|> selectionExpr
            <|> iterationExpr

definitionExpr :: Parser ParseExpr
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
    return $ Def la b

selectionExpr :: Parser ParseExpr
selectionExpr = do
    reserved "if"
    cond <- expression
    reserved "then"
    a <- expression
    reserved "else"
    b <- expression
    reserved "end"
    return $ If cond a b

iterationExpr :: Parser ParseExpr
iterationExpr = do
    reserved "while"
    cond <- expression
    reserved "do"
    bod <- expression
    reserved "end"
    return $ While cond bod


toSyntax :: ParseExpr -> S.Expr a
toSyntax ( LitNum a ) = S.LitNum a
toSyntax ( LitBool a ) = S.LitBool a
toSyntax ( Id a ) = S.Id a
toSyntax ( Add a b ) = S.Add (toSyntax a) (toSyntax b)
toSyntax ( Mul a b ) = S.Mul (toSyntax a) (toSyntax b) 
toSyntax ( UMinus a ) = S.UMinus (toSyntax a)
toSyntax ( LogNeg a ) = S.LogNeg (toSyntax a)
toSyntax ( RelEq a b ) = S.RelEq (toSyntax a) (toSyntax b)
toSyntax ( LogOr a b ) = S.LogOr (toSyntax a) (toSyntax b)
toSyntax ( LogAnd a b ) = S.LogAnd (toSyntax a) (toSyntax b)
toSyntax ( Def l a ) = S.Def (map (second toSyntax) l) (toSyntax a)
toSyntax ( New a ) = S.New (toSyntax a)
toSyntax ( If a b c ) = S.If (toSyntax a) (toSyntax b) (toSyntax c)
toSyntax ( While a b ) = S.While (toSyntax a) (toSyntax b)
toSyntax ( Print a ) = S.Print (toSyntax a)
toSyntax ( Seq a b ) = S.Seq (toSyntax a) (toSyntax b)
toSyntax ( Assign a b ) = S.Assign (toSyntax a) (toSyntax b)
toSyntax ( Deref a ) = S.Deref (toSyntax a)
