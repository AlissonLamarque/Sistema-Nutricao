@echo off
echo === Sistema de Nutricao - Deploy Script (Windows) ===

REM Verificar se o Minikube esta rodando
minikube status | findstr "Running" >nul
if errorlevel 1 (
    echo Iniciando Minikube...
    minikube start
) else (
    echo Minikube ja esta rodando
)

REM Configurar o ambiente Docker para usar o daemon do Minikube
call minikube docker-env

echo === Build da aplicacao Maven ===
REM Build do projeto Maven
call mvn clean package -DskipTests

echo === Build da imagem Docker ===
REM Build da imagem Docker
docker build -t sistema-nutricao:latest .

echo === Aplicando configuracoes Kubernetes ===
REM Aplicar namespace
kubectl apply -f k8s/namespace.yaml

REM Aplicar ConfigMap e Secret
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml

REM Aplicar PersistentVolumeClaims
kubectl apply -f k8s/mysql-pvc.yaml
kubectl apply -f k8s/app-pvc.yaml

REM Aplicar MySQL
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/mysql-service.yaml

echo === Aguardando MySQL estar pronto ===
kubectl wait --for=condition=ready pod -l app=mysql -n nutricao --timeout=300s

echo === Aplicando aplicacao ===
REM Aplicar aplicacao
kubectl apply -f k8s/app-deployment.yaml
kubectl apply -f k8s/app-service.yaml

echo === Aguardando aplicacao estar pronta ===
kubectl wait --for=condition=ready pod -l app=sistema-nutricao -n nutricao --timeout=300s

echo === Deploy concluido! ===
echo Para acessar a aplicacao:
echo minikube service sistema-nutricao-service -n nutricao
echo.
echo Para ver os pods:
echo kubectl get pods -n nutricao
echo.
echo Para ver os logs da aplicacao:
echo kubectl logs -f deployment/sistema-nutricao-deployment -n nutricao

pause 