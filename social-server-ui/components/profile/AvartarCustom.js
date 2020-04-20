
import {Avatar} from "antd";
import React from "react";
import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";
import {baseUrl} from "../../config/config";


const AvartarCustom = ({shape,size, profileImageUrl, name}) => {

    return (
        <Avatar style={{cursor: 'pointer'}} shape={shape} size={size} icon={<img src={`${profileImageUrl}`} alt=""/>}>
            {name && name[0]}
        </Avatar>

    );
};

export default AvartarCustom;