
import { all, fork } from 'redux-saga/effects';
import axios from 'axios'; 
import user from './user'; 
import post from './post'; 


axios.defaults.baseURL = process.env.NODE_ENV === 'production' ?
    'http://ec2-13-125-116-71.ap-northeast-2.compute.amazonaws.com/api/' :
    'http://localhost:8080/api/';

export const baseUrl = process.env.NODE_ENV === 'production' ?
    'http://ec2-13-125-116-71.ap-northeast-2.compute.amazonaws.com/' :
    'http://localhost:8080/';

export default function* rootSaga() {
  yield all([
    fork(user),
    fork(post),
  ]);
}
