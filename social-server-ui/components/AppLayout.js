import React, {useCallback} from 'react';
import Link from 'next/link';
import PropTypes from 'prop-types';
import {
    Menu, Input, Button, Col, Row
} from 'antd';
import {useDispatch, useSelector} from 'react-redux';
import UserProfile from './UserProfile';
import Router from 'next/router';
import {LOG_OUT} from "../reducers/user";
import {DivWrap} from "./styles/ContainerStyle";
import CenterAlignment from "./CenterAlignment";

const AppLayout = ({ children }) => {
    const { me } = useSelector((state) => state.user);
    const dispatch = useDispatch();
    const onSearch = (value) => {
        Router.push({pathname: '/hashtag', query: {tag: value}}, `/hashtag/${value}`);
    };

    const onLogout = useCallback(() => {

        //Router.push('/');
        dispatch({
            type: LOG_OUT,
        });


    }, []);

    return (
    <div>
      <Menu mode="horizontal" theme="dark">

        <Menu.Item key="home"><Link href="/"><a>SNS</a></Link></Menu.Item>
        <Menu.Item key="mail">
          <Input
              enterButton
              style={{ verticalAlign: 'middle' }}
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
          <Menu.Item key="login" style={{ float: 'right' }}>
              <Button onClick={onLogout}>로그아웃</Button>
          </Menu.Item>
        }

        {
          me &&
          <Menu.Item key="profile" style={{ float: 'right' }}>
            <Link href="/profile" prefetch><a>프로필</a></Link>
          </Menu.Item>
        }

      </Menu>
        {children}
    </div>
    );
};
/*<Row gutter={8}>
        <Col xs={24} md={6}>
          {me ? <UserProfile /> : <LoginForm />}
        </Col>
        <Col xs={24} md={12}>
          {children}

        </Col>
        <Col xs={24} md={6} />
      </Row>*/

AppLayout.propTypes = {
  children: PropTypes.node.isRequired,
};



export default AppLayout;
