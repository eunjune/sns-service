import React from 'react';
import PropTypes from 'prop-types';
import Link from 'next/link';

const PostCardContent = ({postData, keyword}) => {


    return (
        <div>
            {
                keyword ?

                    postData.split(new RegExp('(' + keyword + ')'))
                        .map(v => {
                            if (v.match(new RegExp(keyword))) {
                                return (
                                    <span style={{backgroundColor: 'yellow'}}>{v}</span>
                                );
                            }
                            return v;
                        }) :
                    postData.split(/(#[^\s!@#$%^&*()+-=`~.;'"?<>,./]+)/g)
                        .map(v => {
                            if (v.match(/#[^\s!@#$%^&*()+-=`~.;'"?<>,./]+/)) {
                                return (
                                    <Link href={{pathname: '/hashtag', query: {tag: v.slice(1)}}} as={`/hashtag/${v.slice(1)}`} key={v}><a target="_blank">{v}</a></Link>
                                );
                            }
                            return v;
                        })
            }
        </div>

    );

}

PostCardContent.propTypes = {
    postData: PropTypes.string.isRequired,
};

export default PostCardContent;