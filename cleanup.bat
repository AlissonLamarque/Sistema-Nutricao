@echo off
echo === Limpando recursos Kubernetes ===

REM Deletar namespace (isso remove tudo dentro dele)
kubectl delete namespace nutricao

echo === Limpeza concluida ===
echo Todos os recursos do namespace 'nutricao' foram removidos

pause 