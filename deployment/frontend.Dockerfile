FROM node:20-alpine AS build

WORKDIR /workspace

COPY frontend/package.json frontend/package.json
COPY frontend/package-lock.json frontend/package-lock.json
RUN npm --prefix frontend ci

COPY frontend frontend
RUN npm --prefix frontend run build

FROM nginx:1.27-alpine

COPY deployment/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /workspace/frontend/dist /usr/share/nginx/html

EXPOSE 80
