import React from 'react';
import Link from 'next/link';
import PropTypes from 'prop-types';
import {
  Menu, Input, Row, Col, 
} from 'antd';
import { useSelector } from 'react-redux';
import LoginForm from './LoginForm';
import UserProfile from './UserProfile';
import Router from 'next/router';

const AppLayout = ({ children }) => {
  const { me } = useSelector((state) => state.user);

  const onSearch = (value) => {
    Router.push({pathname: '/hashtag', query: {tag: value}}, `/hashtag/${value}`);
  };

  return (
    <div>
      <Menu mode="horizontal">
        
        <Menu.Item key="home"><Link href="/"><a>SNS</a></Link></Menu.Item>
        <Menu.Item key="profile" style={{ float: 'right' }}><Link href="/profile" prefetch><a>프로필</a></Link></Menu.Item>
        <Menu.Item key="mail" style={{ float: 'right' }}>
          <Input.Search 
            enterButton 
            style={{ verticalAlign: 'middle' }} 
            onSearch={onSearch}
          />
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
