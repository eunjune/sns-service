
import {Avatar} from "antd";
import React from "react";
import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";


const AvartarCustom = ({shape,size, profileImageUrl, name}) => {

    return (
        <Avatar style={{cursor: 'pointer'}} shape={shape} size={size} icon={profileImageUrl ?
            <img src={`http://localhost:8080/image/profile/${profileImageUrl}`} alt=""/> :
            (name === null ? <UserOutlined style={{marginRight: 0}} /> : null)
        }>
            {name && name[0]}
        </Avatar>

    );
};

export default AvartarCustom;