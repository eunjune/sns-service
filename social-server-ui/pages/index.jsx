import React from 'react';
import {Form, Input, Button, Card, Icon, Avatar} from 'antd';

const dummy = {
    isLogin: true,
    imagePaths: [],
    mainPosts: [{
        User: {
            id: 1,
            name: '이름'
        },
        content: '첫번째 게시글',
        img: ''
    }],
};

const Home = () => {
    return (
        <div>
            {dummy.isLogin && <Form encType="multipart/form-data" style={{marginBottom: 20}}>
                <Input maxLength={140} placeholder="어떤 일이 있었나요?"/>
                <div>
                    <input type="file" multiple hidden/>
                    <Button>이미지 업로드</Button>
                    <Button type="primary" style={{float: 'right'}} htmlType="submit">짹짹</Button>
                </div>
                <div>
                    {dummy.imagePaths.map((v,i) => {
                        return (
                            <div key={v} style={{display: 'inline-block'}}>
                                <img src={'http://localhost:3065/' + v}  style={{width: '200px'}} alt={v}/>
                                <div>
                                    <Button>제거</Button>
                                </div>
                            </div>
                        );
                    })}
                </div>
            </Form>}
            {dummy.mainPosts.map((c) => {
                return (
                    <Card
                        key={+c.createdAt}
                        cover={c.img && <img src="c.img" alt="example"/>}
                        actions={[
                            <Icon type="retweet" key="retweet"/>,
                            <Icon type="heart" key="heart"/>,
                            <Icon type="message" key="message"/>,
                            <Icon type="ellipsis" key="ellipsis"/>,
                        ]}
                        extra = {<Button>팔로우</Button>}
                    >
                        <Card.Meta
                            avatar={<Avatar>{c.User.name[0]}</Avatar>}
                            title={c.User.name}
                            description={c.content}
                        >

                        </Card.Meta>
                    </Card>
                );
            })}
        </div>
    );
};

export default Home;