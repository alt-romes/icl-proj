{-# LANGUAGE GADTs, StandaloneDeriving #-}
module Syntax where 

data Expr a where
    LitNum :: Integer -> Expr Int
    LitBool :: Bool -> Expr Bool
    Id :: String -> Expr a
    Add :: Expr Int -> Expr Int -> Expr Int
    Mul :: Expr Int -> Expr Int -> Expr Int
    UMinus :: Expr Int -> Expr Int
    RelEq :: Expr a -> Expr a -> Expr Bool
    LogOr :: Expr Bool -> Expr Bool -> Expr Bool
    LogAnd :: Expr Bool -> Expr Bool -> Expr Bool
    LogNeg :: Expr Bool -> Expr Bool
    Def :: [(String, Expr a)] -> Expr a -> Expr a
    New :: Expr a -> Expr (Ref a)
    If :: Expr Bool -> Expr a -> Expr a -> Expr a
    While :: Expr Bool -> Expr a -> Expr Bool
    Print :: Expr a -> Expr a
    Seq :: Expr a -> Expr b -> Expr b
    Assign :: Expr (Ref a) -> Expr b -> Expr b
    Deref :: Expr (Ref a) -> Expr a

deriving instance Show (Expr a)

data Ref a = Re deriving (Show)

