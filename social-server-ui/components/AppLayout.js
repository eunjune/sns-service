import React, {useCallback} from 'react';
import Link from 'next/link';
import PropTypes from 'prop-types';
import {
    Menu, Input, Dropdown, Avatar,
} from 'antd';
import {useDispatch, useSelector} from 'react-redux';
import Router from 'next/router';
import {LOG_OUT} from "../reducers/user";
import { DownOutlined,BellOutlined } from '@ant-design/icons';
import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";
import {MenuItem} from "./styles/MenuItemStyle";

const AppLayout = ({ children }) => {
    const { me } = useSelector((state) => state.user);
    const dispatch = useDispatch();
    const onSearch = (value) => {
        Router.push({pathname: '/hashtag', query: {tag: value}}, `/hashtag/${value}`);
    };


    const onLogout = useCallback(() => {
        dispatch({
            type: LOG_OUT,
        });
    }, []);

    return (
    <div>
      <Menu mode="horizontal" theme="dark">

        <MenuItem key="home"><Link href="/"><a>SNS</a></Link></MenuItem>
        <MenuItem key="mail" >
          <Input.Search
              style={{width: 400,  verticalAlign: 'middle' }}
              onSearch={onSearch}
          />
        </MenuItem>

        {
            !me &&
          <MenuItem key="signup" style={{ float: 'right'}}>
            <Link href="/signup" prefetch><a>회원가입</a></Link>
          </MenuItem>
        }

        {
          !me &&
          <MenuItem key="login" style={{ float: 'right'}}>
            <Link href="/login" prefetch><a>로그인</a></Link>
          </MenuItem>
        }

        {
          me &&
          <MenuItem key="profile" style={{ float: 'right' }}>
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
                      <Avatar shape="square" size="small" icon={me.profileImageUrl ?
                          <img src={`http://localhost:8080/image/profile/${me.profileImageUrl}`} alt=""/> :
                          <UserOutlined style={{marginRight: 0}} />} />
                          <DownOutlined/>

                  </a>
              </Dropdown>
          </MenuItem>
        }

          {
              me &&
              <MenuItem key="notification" style={{ float: 'right', padding: 0}}>
                <BellOutlined style={{margin: 'auto'}}/>
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
