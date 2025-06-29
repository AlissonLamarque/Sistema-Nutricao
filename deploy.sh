#!/bin/bash

echo "=== Sistema de Nutrição - Deploy Script ==="

# Verificar se o Minikube está rodando
if ! minikube status | grep -q "Running"; then
    echo "Iniciando Minikube..."
    minikube start
else
    echo "Minikube já está rodando"
fi

# Configurar o ambiente Docker para usar o daemon do Minikube
eval $(minikube docker-env)

echo "=== Build da aplicação Maven ==="
# Build do projeto Maven
mvn clean package -DskipTests

echo "=== Build da imagem Docker ==="
# Build da imagem Docker
docker build -t sistema-nutricao:latest .

echo "=== Aplicando configurações Kubernetes ==="
# Aplicar namespace
kubectl apply -f k8s/namespace.yaml

# Aplicar ConfigMap e Secret
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml

# Aplicar PersistentVolumeClaims
kubectl apply -f k8s/mysql-pvc.yaml
kubectl apply -f k8s/app-pvc.yaml

# Aplicar MySQL
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/mysql-service.yaml

echo "=== Aguardando MySQL estar pronto ==="
kubectl wait --for=condition=ready pod -l app=mysql -n nutricao --timeout=300s

echo "=== Aplicando aplicação ==="
# Aplicar aplicação
kubectl apply -f k8s/app-deployment.yaml
kubectl apply -f k8s/app-service.yaml

echo "=== Aguardando aplicação estar pronta ==="
kubectl wait --for=condition=ready pod -l app=sistema-nutricao -n nutricao --timeout=300s

echo "=== Deploy concluído! ==="
echo "Para acessar a aplicação:"
echo "minikube service sistema-nutricao-service -n nutricao"
echo ""
echo "Para ver os pods:"
echo "kubectl get pods -n nutricao"
echo ""
echo "Para ver os logs da aplicação:"
echo "kubectl logs -f deployment/sistema-nutricao-deployment -n nutricao" 