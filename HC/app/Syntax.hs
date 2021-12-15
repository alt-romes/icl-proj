{-# LANGUAGE GADTs #-}
module Syntax where 

data Expr a where
    LitNum :: Int -> Expr Int
    LitBool :: Bool -> Expr Bool
    Id :: String -> Expr String
    Add :: Expr Int -> Expr Int -> Expr Int
    Mul :: Expr Int -> Expr Int -> Expr Int
    UMinus :: Expr Int -> Expr Int
    RelEq :: Expr a -> Expr a -> Expr Bool
    RelGt :: Expr a -> Expr a -> Expr Bool
    LogOr :: Expr Bool -> Expr Bool -> Expr Bool
    LogAnd :: Expr Bool -> Expr Bool -> Expr Bool
    LogNeg :: Expr Bool -> Expr Bool
    Def :: [(String, Expr a)] -> Expr a -> Expr a

