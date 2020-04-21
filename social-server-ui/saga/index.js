import {all, fork} from 'redux-saga/effects';
import axios from 'axios';
import user from './user';
import post from './post';
import {baseUrl} from "../config/config";


axios.defaults.baseURL = `${baseUrl}/api`

export default function* rootSaga() {
    yield all([
        fork(user),
        fork(post),
    ]);
}
