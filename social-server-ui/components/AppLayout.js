import React, {useCallback} from 'react';
import Link from 'next/link';
import PropTypes from 'prop-types';
import {
    Menu, Input, Button, Dropdown,
} from 'antd';
import {useDispatch, useSelector} from 'react-redux';
import UserProfile from './UserProfile';
import Router from 'next/router';
import {LOG_OUT} from "../reducers/user";
import {DivWrap} from "./styles/ContainerStyle";
import CenterAlignment from "./CenterAlignment";
import { DownOutlined } from '@ant-design/icons';

const AppLayout = ({ children }) => {
    const { me } = useSelector((state) => state.user);
    const dispatch = useDispatch();
    const onSearch = (value) => {
        Router.push({pathname: '/hashtag', query: {tag: value}}, `/hashtag/${value}`);
    };

    const onLogout = useCallback(() => {

        Router.push('/');
        dispatch({
            type: LOG_OUT,
        });


    }, []);

    return (
    <div>
      <Menu mode="horizontal" theme="dark">

        <Menu.Item key="home"><Link href="/"><a>SNS</a></Link></Menu.Item>
        <Menu.Item key="mail">
          <Input.Search
              style={{width: 400,  verticalAlign: 'middle' }}
              onSearch={onSearch}
          />
        </Menu.Item>

        {
            !me &&
          <Menu.Item key="signup" style={{ float: 'right' }}>
            <Link href="/signup" prefetch><a>회원가입</a></Link>
          </Menu.Item>
        }

        {
          !me &&
          <Menu.Item key="login" style={{ float: 'right' }}>
            <Link href="/login" prefetch><a>로그인</a></Link>
          </Menu.Item>
        }


        {
          me &&
          <Menu.Item key="profile" style={{ float: 'right' }}>
              <Dropdown overlay={
                  <Menu>
                      <Menu.Item key="0">
                          <Link href="/profile" prefetch><a>프로필</a></Link>
                      </Menu.Item>
                      <Menu.Divider />
                      <Menu.Item key="2">
                          <a>설정</a>
                      </Menu.Item>
                      <Menu.Item key="3">
                          <a onClick={onLogout}>로그아웃</a>
                      </Menu.Item>
                  </Menu>
              } trigger={['click']}>
                  <a className="ant-dropdown-link" onClick={e => e.preventDefault()}>
                      프로필 <DownOutlined  />
                  </a>
              </Dropdown>
          </Menu.Item>
        }

      </Menu>
        {children}
    </div>
    );
};

AppLayout.propTypes = {
  children: PropTypes.node.isRequired,
};



export default AppLayout;
