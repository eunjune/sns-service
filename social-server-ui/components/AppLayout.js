import React, { useEffect } from 'react';
import Link from 'next/link';
import PropTypes from 'prop-types';
import {
  Menu, Input, Row, Col, 
} from 'antd';
import { useDispatch, useSelector } from 'react-redux';
import LoginForm from './LoginForm';
import UserProfile from './UserProfile';
import { LOAD_ME_REQUEST } from '../reducers/user';

const AppLayout = ({ children }) => {
  const { me } = useSelector((state) => state.user);
  const dispatch = useDispatch();

  useEffect(() => {
    const token = sessionStorage.getItem('token');

    if (!me) {
      dispatch({
        type: LOAD_ME_REQUEST,
        data: token,
      });
    }
  }, []);

  return (
    <div>
      <Menu mode="horizontal">
        
        <Menu.Item key="home"><Link href="/"><a>SNS</a></Link></Menu.Item>
        <Menu.Item key="profile" style={{ float: 'right' }}><Link href="/profile"><a>프로필</a></Link></Menu.Item>
        <Menu.Item key="mail" style={{ float: 'right' }}>
          <Input.Search enterButton style={{ verticalAlign: 'middle' }} />
        </Menu.Item>
      </Menu>
      <Row gutter={8}>
        <Col xs={24} md={6}>
          {me ? <UserProfile /> : <LoginForm />}
        </Col>
        <Col xs={24} md={12}>
          {children}

        </Col>
        <Col xs={24} md={6} />
      </Row>
    </div>
  );
};

AppLayout.propTypes = {
  children: PropTypes.node.isRequired,
};

export default AppLayout;
