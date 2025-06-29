#!/bin/bash

echo "=== Limpando recursos Kubernetes ==="

# Deletar namespace (isso remove tudo dentro dele)
kubectl delete namespace nutricao

echo "=== Limpeza concluída ==="
echo "Todos os recursos do namespace 'nutricao' foram removidos" 