import React, {useCallback} from 'react';
import Link from 'next/link';
import PropTypes from 'prop-types';
import {
    Menu, Input, Dropdown, Badge, Avatar,
} from 'antd';
import {useDispatch, useSelector} from 'react-redux';
import Router from 'next/router';
import {LOG_OUT, RESET} from "../reducers/user";
import {DownOutlined, BellOutlined} from '@ant-design/icons';
import {MenuItem} from "./styles/MenuItemStyle";
import AvartarCustom from "./profile/AvartarCustom";
import NotificationOutlined from "@ant-design/icons/lib/icons/NotificationOutlined";

const AppLayout = ({children}) => {
    const {me,newNotifications} = useSelector((state) => state.user);
    const dispatch = useDispatch();
    const onSearch = (value) => {
        Router.push({pathname: '/search', query: {keyword: value}}, `/search/${value}`);
    };

    const onLogout = useCallback(() => {
        dispatch({
            type: LOG_OUT,
        });

        dispatch({
            type: RESET
        });
    }, []);

    const onReset = useCallback(() => {
        Router.push('/');
        dispatch({
            type: RESET
        });
    }, []);

    return (
        <div>
            <Menu mode="horizontal" theme="dark">

                <MenuItem key="home"><a onClick={onReset}>SNS</a></MenuItem>
                <MenuItem key="mail">
                    <Input.Search
                        style={{width: 400, verticalAlign: 'middle'}}
                        onSearch={onSearch}
                    />
                </MenuItem>

                {
                    !me &&
                    <MenuItem key="signup" style={{float: 'right'}}>
                        <Link href="/signup" prefetch><a>회원가입</a></Link>
                    </MenuItem>
                }

                {
                    !me &&
                    <MenuItem key="login" style={{float: 'right'}}>
                        <Link href="/login" prefetch><a>로그인</a></Link>
                    </MenuItem>
                }

                {
                    me &&
                    <MenuItem key="profile" style={{float: 'right'}}>
                        <Dropdown overlay={
                            <Menu>
                                <Menu.Item key="0">

                                    <Link href="/profile" prefetch><a>프로필</a></Link>
                                </Menu.Item>
                                <Menu.Divider/>
                                <Menu.Item key="2">
                                    <a>설정</a>
                                </Menu.Item>
                                <Menu.Item key="3">
                                    <a onClick={onLogout}>로그아웃</a>
                                </Menu.Item>
                            </Menu>
                        } trigger={['click']}>
                            <a className="ant-dropdown-link" onClick={e => e.preventDefault()}>
                                <Avatar style={{cursor: 'pointer'}} shape="square" size="small" icon={<img src={me && me.profileImageUrl} alt=""/>} />
                                <DownOutlined/>
                            </a>
                        </Dropdown>
                    </MenuItem>
                }

                {
                    me &&
                    <MenuItem key="notification" style={{float: 'right', padding: 0}}>
                        <Link href="/notification" prefetch>
                            <div>
                                <Badge count={me.notificationCount} dot>
                                    <NotificationOutlined />
                                </Badge>
                            </div>
                        </Link>
                    </MenuItem>
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
