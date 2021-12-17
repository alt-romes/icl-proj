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
                                       , "==", ">", "&&", "||", "~", "!"
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

-- Would be simpler to use an expressions parser built in to parsec but...

data ParseExpr a = EB (Expr Bool)
                 | ER (Expr (Ref a))
                 | EI (Expr Int)
                 | EA (Expr a)

parseExpr :: Parser (ParseExpr a)
parseExpr = whiteSpace >> expression

expression :: Parser (ParseExpr a)
expression = sequenceExpr

sequenceExpr :: Parser (ParseExpr a)
sequenceExpr = (do
    EA a <- assignmentExpr
    semi
    EA b <- assignmentExpr
    return $ EA (Seq a b)) <|> assignmentExpr

assignmentExpr :: Parser (ParseExpr a)
assignmentExpr = (do
    ER a <- logicalOrExpr
    reservedOp ":="
    EA b <- logicalOrExpr
    return $ EA (Assign a b)) <|> logicalOrExpr

logicalOrExpr :: Parser (ParseExpr a)
logicalOrExpr = (do
    EB a <- logicalAndExpr
    reservedOp "||"
    EB b <- logicalAndExpr
    return $ EB (LogOr a b)) <|> logicalAndExpr

logicalAndExpr :: Parser (ParseExpr a)
logicalAndExpr = (do
    EB a <- relationalExpr
    reservedOp "&&"
    EB b <- relationalExpr
    return $ EB (LogAnd a b)) <|> relationalExpr

relationalExpr :: Parser (ParseExpr a)
relationalExpr = (do
    EA a <- additiveExpr
    reservedOp "=="
    EA b <- additiveExpr
    return $ EB (RelEq a b)) <|> additiveExpr

additiveExpr :: Parser (ParseExpr a)
additiveExpr = (do
    EI a <- multiplicativeExpr
    reservedOp "+"
    EI b <- multiplicativeExpr
    return $ EI (Add a b)) <|> multiplicativeExpr

multiplicativeExpr :: Parser (ParseExpr a)
multiplicativeExpr = (do
    EI a <- unaryExpr
    reservedOp "*"
    EI b <- unaryExpr
    return $ EI (Mul a b)) <|> unaryExpr

unaryExpr :: Parser (ParseExpr a)
unaryExpr = (do
    reservedOp "-"
    EI a <- primaryExpr
    return $ EI (UMinus a))
    <|>
    (do
    reservedOp "~"
    EB a <- primaryExpr
    return $ EB (LogNeg a))
    <|>
    (do
    reservedOp "new"
    EA a <- primaryExpr
    return $ ER (New a))
    <|>
    (do
    reservedOp "print"
    EA a <- primaryExpr
    return $ ER (New a))
    <|>
    (do
    reservedOp "print"
    EA a <- primaryExpr
    return $ ER (New a))
    <|>
    (do
    reservedOp "!"
    ER a <- primaryExpr
    return $ EA (Deref a))
    <|>
    definitionExpr
    <|>
    selectionExpr
    <|>
    iterationExpr

primaryExpr :: Parser (ParseExpr a)
primaryExpr = ...
