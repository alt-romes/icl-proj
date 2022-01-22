{-# LANGUAGE GADTs, TypeFamilies #-}
module Typechecker where

import Parser
import Syntax
import Control.Monad
import Data.Bifunctor

-- TODO, perceber melhor o existencial wrapper funcionar

type Env = [(String, ATExpr)]

typecheck :: Env -> UExpr -> Maybe ATExpr
typecheck e ( ULitNum a ) = return $ TLitNum a ::: TTInt
typecheck e ( ULitBool a ) = return $ TLitBool a ::: TTBool
typecheck e ( UId a ) = lookup a e
typecheck e ( UAdd a b ) = do
    a ::: TTInt <- typecheck e a
    b ::: TTInt <- typecheck e b
    return $ TAdd a b ::: TTInt
typecheck e ( UMul a b ) = do
    a ::: TTInt <- typecheck e a
    b ::: TTInt <- typecheck e b
    return $ TMul a b ::: TTInt
typecheck e ( UUMinus a ) = do
    a ::: TTInt <- typecheck e a
    return $ TUMinus a ::: TTInt
typecheck e ( ULogNeg a ) = do
    a ::: TTBool <- typecheck e a
    return $ TLogNeg a ::: TTBool
typecheck e ( URelEq a b ) = do
    a ::: ta <- typecheck e a
    b ::: tb <- typecheck e b
    Eq <- test ta tb
    return $ TRelEq a b ::: TTBool
typecheck e ( ULogOr a b ) = do
    a ::: TTBool <- typecheck e a
    b ::: TTBool <- typecheck e b
    return $ TLogOr a b ::: TTBool
typecheck e ( ULogAnd a b ) = do
    a ::: TTBool <- typecheck e a
    b ::: TTBool <- typecheck e b
    return $ TLogAnd a b ::: TTBool
typecheck e ( UDef l a ) = do
    (newEnv, texpList) <- foldM (\(newEnv, texpList) (id, uexp) -> do
            ate@(texp ::: tt) <- typecheck newEnv uexp
            return ((id, ate):newEnv, (id, ate):texpList)
        ) (e, []) l
    a ::: ta <- typecheck newEnv a
    return $ TDef texpList a ::: ta
typecheck e ( UNew a ) = do
    a ::: ta <- typecheck e a
    return $ TNew a ::: TTRef ta
typecheck e ( UIf a b c ) = do
    a ::: TTBool <- typecheck e a
    b ::: tb <- typecheck e b
    c ::: tc <- typecheck e c
    Eq <- test tb tc
    return $ TIf a b c ::: tb
typecheck e ( UWhile a b ) = do
    a ::: TTBool <- typecheck e a
    b ::: _ <- typecheck e b
    return $ TWhile a b ::: TTBool
typecheck e ( UPrint a ) = do
    a ::: ta <- typecheck e a
    return $ TPrint a ::: ta
typecheck e ( USeq a b ) = do
    a ::: _ <- typecheck e a
    b ::: tb <- typecheck e b
    return $ TSeq a b ::: tb
typecheck e ( UAssign a b ) = do
    a ::: TTRef _ <- typecheck e a
    b ::: tb <- typecheck e b
    return $ TAssign a b ::: tb
typecheck e ( UDeref a ) = do
    a ::: TTRef tia <- typecheck e a
    return $ TDeref a ::: tia

-- "Standard" trick for testing equality in this situation
data Equal a b where
    Eq :: Equal a a

test :: TType a -> TType b -> Maybe (Equal a b)
test TTBool TTBool = return Eq
test TTInt TTInt = return Eq
test _ _ = mzero
