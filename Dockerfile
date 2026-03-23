FROM nginx:alpine

WORKDIR /app

COPY frontend /usr/share/nginx/html

RUN npm install

COPY frontend .

EXPOSE 3000

CMD ["npm", "start"]
