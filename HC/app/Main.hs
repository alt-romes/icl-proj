module Main where

import Syntax
import Parser
import Text.ParserCombinators.Parsec

parseString :: String -> ParseExpr a
parseString str =
  case parse parseExpr  "" str of
    Left e  -> error $ show e
    Right r -> r

main :: IO ()
main = putStrLn "Hello, Haskell!"

