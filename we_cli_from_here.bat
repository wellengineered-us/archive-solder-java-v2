@echo off

REM
REM	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
REM	Distributed under the MIT license: http://www.opensource.org/licenses/mit-license.php
REM

CALL we_set_mvn_env.bat
CALL we_set_py_env.bat

cd "."
%comspec% /k "cd ."
