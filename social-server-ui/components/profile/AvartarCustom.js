import {Avatar} from "antd";
import React from "react";
import Link from "next/link";


const AvartarCustom = ({shape, size, profileImageUrl,id}) => {

    return (

        <Link href={{pathname: '/user', query: {id: id}}} as={`/user/${id}`}>
            <Avatar style={{cursor: 'pointer'}} shape={shape} size={size} icon={<img src={`${profileImageUrl}`} alt=""/>} />
        </Link>
    );
};

export default AvartarCustom;