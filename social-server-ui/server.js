const express = require('express');
const next = require('next');
const morgan = require('morgan');
const cookieParser = require('cookie-parser');
const expressSession = require('express-session');
const dotenv = require('dotenv');
const path = require('path');

const dev = process.env.NODE_ENV !== 'production';
const prod = process.env.NODE_ENV === 'production';

const app = next({dev});
const handle = app.getRequestHandler();
dotenv.config();
app.prepare().then(() => {

  const server = express();

  server.use(express.static('public'));
  server.use(morgan('dev'));
  server.use(express.json());
  server.use(express.urlencoded({extended: true}));


  server.get('/user/:userId/post/:postId', (req,res) => {
    return app.render(req,res, '/post', {userId : req.params.userId, postId : req.params.postId});
  });

  server.get('/hashtag/:tag', (req,res) => {
    return app.render(req,res, '/hashtag', {tag : req.params.tag});
  });

  server.get('/search/:keyword', (req,res) => {
    return app.render(req,res, '/search', {keyword : req.params.keyword});
  });

  server.get('/check-email-token', (req,res) => {
    return app.render(req,res,'/certification',{emailToken : req.query.token, email: req.query.email});
  });

  server.get('/login-link', (req,res) => {
    return app.render(req,res,'/',{token: req.query.apiToken});
  });

  server.get('/user/:id', (req,res) => {
    return app.render(req,res,'/user', {id: req.params.id});
  });

  server.get('*', (req,res) => {
    return handle(req,res);
  });

  server.listen(prod ? process.env.PORT : 3060, () => {
    console.log(`next+express running on port ${process.env.PORT}`);
  });
});
