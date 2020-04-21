import {Button, Card, Icon, List} from "antd";
import React, {memo} from "react";
import PropTypes from 'prop-types';
import Link from "next/link";

const FollowList = memo(({header, hasMore, onClickMore, data, onClickStop}) => {

    return (
        <List
            style={{marginBottom: '20px'}}
            grid={{gutter: 4, xs: 2, md: 3}}
            size="small"
            header={<div>{header}</div>}
            loadMore={hasMore && <Button style={{width: '100%'}} onClick={onClickMore}>더 보기</Button>}
            bordered
            dataSource={data}
            renderItem={item => (
                <List.Item style={{marginTop: '20px'}}>
                    <Card actions={[<Icon key="stop" type="stop" onClick={onClickStop(item.id)}/>]}>
                        <Card.Meta description={
                            <Link href={{pathname: '/user', query: {id: item.id}}} as={`/user/${item.id}`}>
                                <a>{item.name}</a>
                            </Link>
                        }
                        />
                    </Card>
                </List.Item>
            )}
        />
    );
});

FollowList.propTypes = {
    header: PropTypes.string.isRequired,
    hasMore: PropTypes.bool.isRequired,
    onClickMore: PropTypes.func.isRequired,
    data: PropTypes.array.isRequired,
    onClickStop: PropTypes.func.isRequired,
};

export default FollowList;