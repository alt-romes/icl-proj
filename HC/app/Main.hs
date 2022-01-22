module Main where

import Syntax
import Parser
import Typechecker
import Text.ParserCombinators.Parsec

parseString :: String -> Maybe ATExpr
parseString str =
  case parse parseExpr  "" str of
    Left e  -> error $ show e
    Right r -> typecheck [] r

main :: IO ()
main = putStrLn "Hello, Haskell!"

