FROM nginx:alpine

WORKDIR /app

COPY frontend /usr/share/nginx/html

RUN npm install

COPY frontend .

EXPOSE 80

CMD ["npm", "start"]
