{-# LANGUAGE GADTs, StandaloneDeriving, FlexibleInstances #-}
module Syntax where 

import Prelude hiding ((&&), (||), not, (==), (/=))
import qualified Prelude

data TExpr a where
    TLitNum :: Integer -> TExpr Int
    TLitBool :: Bool -> TExpr Bool
    TId :: String -> TExpr String
    TAdd :: TExpr Int -> TExpr Int -> TExpr Int
    TMul :: TExpr Int -> TExpr Int -> TExpr Int
    TUMinus :: TExpr Int -> TExpr Int
    TRelEq :: Eq a => TExpr a -> TExpr a -> TExpr Bool
    TLogOr :: TExpr Bool -> TExpr Bool -> TExpr Bool
    TLogAnd :: TExpr Bool -> TExpr Bool -> TExpr Bool
    TLogNeg :: TExpr Bool -> TExpr Bool
    TDef :: [(String, ATExpr)] -> TExpr b -> TExpr b
    TNew :: TExpr a -> TExpr (Ref a)
    TIf :: TExpr Bool -> TExpr a -> TExpr a -> TExpr a
    TWhile :: TExpr Bool -> TExpr a -> TExpr Bool
    TPrint :: TExpr a -> TExpr a
    TSeq :: TExpr a -> TExpr b -> TExpr b
    TAssign :: TExpr (Ref a) -> TExpr b -> TExpr b
    TDeref :: TExpr (Ref a) -> TExpr a
deriving instance Show (TExpr a)

data TType a where
    TTBool :: TType Bool
    TTInt  :: TType Int
    TTRef  :: TType a -> TType (Ref a)
deriving instance Show (TType a)

data TValue a where
    TVBool :: Bool -> TValue Bool
    TVInt :: Int -> TValue Int
    TVRef :: TValue a -> TValue (Ref a)
deriving instance Show (TValue a)

newtype Ref a = Ref a deriving (Show, Eq)

data ATExpr = forall a. TExpr a ::: TType a
deriving instance Show ATExpr

data ATValue = forall a. V (TValue a)

instance Num ATValue where
    (V (TVInt a)) + (V (TVInt b)) = V $ TVInt $ a + b
    (V (TVInt a)) * (V (TVInt b)) = V $ TVInt $ a * b
    (V (TVInt a)) - (V (TVInt b)) = V $ TVInt $ a - b
    abs (V (TVInt a)) = V $ TVInt (abs a)
    signum (V (TVInt a)) = V $ TVInt (signum a)
    fromInteger a = V $ TVInt (fromInteger a)

class Boolean a where
    (&&) :: a -> a -> a
    (||) :: a -> a -> a
    not  :: a -> a
infixr 3 &&
infixr 2 ||

instance Boolean Bool where
    (&&) = (Prelude.&&)
    (||) = (Prelude.||)
    not = Prelude.not

instance Boolean ATValue where
    (V (TVBool a)) && (V (TVBool b)) = V $ TVBool $ a && b
    (V (TVBool a)) || (V (TVBool b)) = V $ TVBool $ a || b
    not (V (TVBool a)) = V $ TVBool $ not a
