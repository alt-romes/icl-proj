{-# LANGUAGE TypeFamilies, ExistentialQuantification #-}
module Interpreter where

import Prelude hiding ((&&), (||), not)
import qualified Prelude
import Syntax
import Data.Maybe

type Env = [(String, ATValue)]

eval :: Env -> TExpr a -> ATValue
eval e ( TLitNum a ) = vint . fromInteger $ a
eval e ( TLitBool a ) = vbool a
eval e ( TId a ) = fromJust $ lookup a e
eval e ( TAdd a b ) = eval e a + eval e b
eval e ( TMul a b ) = eval e a * eval e b
eval e ( TUMinus a ) = - eval e a
eval e ( TLogNeg a ) = not $ eval e a
eval e ( TRelEq a b ) = vbool $ eval e a == eval e b
eval e ( TLogOr a b ) = eval e a || eval e b
eval e ( TLogAnd a b ) = eval e a && eval e b
eval e ( TDef l a ) = do
    (newEnv, texpList) <- foldM (\(newEnv, texpList) (id, uexp) -> do
            ate@(texp ::: tt) <- eval newEnv uexp
            return ((id, ate):newEnv, (id, ate):texpList)
        ) (e, []) l
    a ::: ta <- eval newEnv a
    return $ TDef texpList a ::: ta
eval e ( TNew a ) = vref $ eval e a
eval e ( TIf a b c ) = if eval e a == V (TVBool True) then eval e b else eval e c
eval e ( TWhile a b ) = do
    a ::: TTBool <- eval e a
    b ::: _ <- eval e b
    return $ TWhile a b ::: TTBool
eval e ( TPrint a ) = do
    a ::: ta <- eval e a
    return $ TPrint a ::: ta
eval e ( TSeq a b ) = do
    a ::: _ <- eval e a
    b ::: tb <- eval e b
    return $ TSeq a b ::: tb
eval e ( TAssign a b ) = do
    a ::: TTRef _ <- eval e a
    b ::: tb <- eval e b
    return $ TAssign a b ::: tb
eval e ( TDeref a ) = do
    a ::: TTRef tia <- eval e a
    return $ TDeref a ::: tia

vint = V . TVInt
vbool = V . TVBool
vref = V . TVRef

