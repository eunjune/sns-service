import React from 'react';
import Link from 'next/link';
import PropTypes from 'prop-types';
import {Menu, Input, Button, Row, Col} from 'antd';
import LoginForm from '../components/LoginForm'
import UserProfile from "./UserProfile";
import {useSelector} from "react-redux";

const AppLayout = ({children}) => {

    const {isLogin} = useSelector(state => state.user);

    return (
        <div>
            <Menu mode="horizontal">
                <Menu.Item key="home"><Link href="/"><a>SNS</a></Link></Menu.Item>
                <Menu.Item key="profile" style={{float: 'right'}}><Link href="/profile"><a>프로필</a></Link></Menu.Item>
                <Menu.Item key="mail" style={{float: 'right'}} >
                    <Input.Search enterButton style={{verticalAlign: 'middle'}}/>
                </Menu.Item>
            </Menu>
            <Row gutter={8}>
                {<Col xs={24} md={6}>
                    {isLogin ? <UserProfile/> : <LoginForm />}
                </Col>}
                <Col xs={24} md={12}>
                  {children}

                </Col>
                {<Col xs={24} md={6}>

                </Col>}
            </Row>
        </div>
    );
};

AppLayout.propTypes = {
    children: PropTypes.node,
};

export default AppLayout;
