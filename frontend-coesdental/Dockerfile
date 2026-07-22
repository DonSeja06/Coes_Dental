# Etapa de compilación
FROM node:20-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build -- --configuration production

# Etapa de servidor Nginx
FROM nginx:alpine
# Copiar el build al directorio de nginx
COPY --from=build /app/dist/frontend-coesdental/browser /usr/share/nginx/html
# Copiar configuración personalizada (para manejar rutas de Angular)
RUN echo 'server { \
    listen 80; \
    location / { \
        root /usr/share/nginx/html; \
        index index.html index.htm; \
        try_files $uri $uri/ /index.html; \
    } \
}' > /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
