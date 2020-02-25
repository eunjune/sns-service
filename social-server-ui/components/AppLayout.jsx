import React from 'react';
import Link from 'next/link';
import PropTypes from 'prop-types';
import {Menu, Input, Button, Row, Col, Card, Avatar} from 'antd';
import LoginForm from '../components/LoginForm'
import UserProfile from "./UserProfile";

const dummy = {
    name: '이름' +
        '' +
        '',
    Post: [],
    Followings: [],
    Followers: [],
    isLogin: false,
};

const AppLayout = ({children}) => {

    return (
        <div>
            <Menu mode="horizontal">
                <Menu.Item key="home"><Link href="/"><a>SNS</a></Link></Menu.Item>
                <Menu.Item key="profile"><Link href="/profile"><a>프로필</a></Link></Menu.Item>
                <Menu.Item key="mail">
                    <Input.Search enterButton style={{verticalAlign: 'middle'}}/>
                </Menu.Item>
            </Menu>
            <Link href="/signup"><Button>회원가입</Button></Link>
            <Row gutter={8}>
                <Col xs={24} md={6}>
                    {dummy.isLogin ? <UserProfile/> : <LoginForm />}
                </Col>
                <Col xs={24} md={12}>
                    {children}
                </Col>
                <Col xs={24} md={6}>

                </Col>
            </Row>

        </div>
    );
};

AppLayout.propTypes = {
    children: PropTypes.node,
}

export default AppLayout;