import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";
import {Avatar} from "antd";
import React from "react";


const AvartarCustom = ({shape,size, profileImageUrl, name}) => {

    console.log('avartar');
    console.log(name);
    return (
        <Avatar shape={shape} size={size} icon={profileImageUrl ?
            <img src={`http://localhost:8080/image/profile/${profileImageUrl}`} alt=""/> :
            (name === null ? <UserOutlined style={{marginRight: 0}} /> : null)
        }>
            {name && name[0]}
        </Avatar>

    );
};

export default AvartarCustom;