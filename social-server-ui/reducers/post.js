const dummyPost = {
    id: 1,
    User: {
        id: 1,
        name: '이름',
    },
    content : '나는 더미',
    Comments: [],
};

const dummyComment = {
    id: 2,
    User: {
        id: 1,
        name: 2,
    },
    createdAt: new Date(),
    content: '더미 입니다.',
};

export const initialState = {
    mainPosts: [{
        id : 1,
        User: {
            id: 1,
            name: '이름'
        },
        content: '첫번째 게시글',
        img: '',
        Comments: [],
    }],
    imagePaths: [],
    isAddingPost: false,
    addedPost: false,
    addPostError: false,
    addPostErrorReason: '',
    isAddingComment: false,
    addedComment: false,
    addCommentError: false,
    addCommentErrorReason: '',
};

export const ADD_POST_REQUEST = 'ADD_POST_REQUEST';
export const ADD_POST_SUCCESS = 'ADD_POST_SUCCESS';
export const ADD_POST_FAILURE = 'ADD_POST_FAILURE';

export const LOAD_MAIN_POSTS_REQUEST = 'LOAD_MAIN_POSTS_REQUEST';
export const LOAD_MAIN_POSTS_SUCCESS = 'LOAD_MAIN_POSTS_SUCCESS';
export const LOAD_MAIN_POSTS_FAILURE = 'LOAD_MAIN_POSTS_FAILURE';

export const LOAD_HASHTAG_POSTS_REQUEST = 'LOAD_HASHTAG_POSTS_REQUEST';
export const LOAD_HASHTAG_POSTS_SUCCESS = 'LOAD_HASHTAG_POSTS_SUCCESS';
export const LOAD_HASHTAG_POSTS_FAILURE = 'LOAD_HASHTAG_POSTS_FAILURE';

export const LOAD_USER_POSTS_REQUEST = 'LOAD_USER_POSTS_REQUEST';
export const LOAD_USER_POSTS_SUCCESS = 'LOAD_USER_POSTS_SUCCESS';
export const LOAD_USER_POSTS_FAILURE = 'LOAD_USER_POSTS_FAILURE';

export const UPLOAD_IMAGES_REQUEST = 'UPLOAD_IMAGES_REQUEST';
export const UPLOAD_IMAGES_SUCCESS = 'UPLOAD_IMAGES_SUCCESS';
export const UPLOAD_IMAGES_FAILURE = 'UPLOAD_IMAGES_FAILURE';

export const REMOVE_IMAGE = 'REMOVE_IMAGE';

export const ADD_COMMENT_REQUEST = 'ADD_COMMENT_REQUEST';
export const ADD_COMMENT_SUCCESS = 'ADD_COMMENT_SUCCESS';
export const ADD_COMMENT_FAILURE = 'ADD_COMMENT_FAILURE';

export const LOAD_COMMENTS_REQUEST = 'LOAD_COMMENTS_REQUEST';
export const LOAD_COMMENTS_SUCCESS = 'LOAD_COMMENTS_SUCCESS';
export const LOAD_COMMENTS_FAILURE = 'LOAD_COMMENTS_FAILURE';

export const RETWEET_REQUEST = 'RETWEET_REQUEST';
export const RETWEET_SUCCESS = 'RETWEET_SUCCESS';
export const RETWEET_FAILURE = 'RETWEET_FAILURE';

export const REMOVE_POST_REQUEST = 'REMOVE_POST_REQUEST';
export const REMOVE_POST_SUCCESS = 'REMOVE_POST_SUCCESS';
export const REMOVE_POST_FAILURE = 'REMOVE_POST_FAILURE';

const addPost = {
    type: ADD_POST_REQUEST,
};

const addDummy = {
    type: '',
    data: {
        content: 'Hello',
        UserId: 1,
        User: {
            name: '이름',
        },

    }
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case ADD_POST_REQUEST: {
            return {
                ...state,
                isAddingPost: true,
            }
        }
        case ADD_POST_SUCCESS: {
            return {
                ...state,
                isAddingPost: false,
                mainPosts: [dummyPost, ...state.mainPosts],
            }
        }
        case ADD_POST_FAILURE: {
            return {
                ...state,
                isAddingPost: false,
                addPostErrorReason: action.error,
            }
        }

        case ADD_COMMENT_REQUEST: {
            return {
                ...state,
                isAddingComment: true,
                addCommentErrorReason: '',
                addedComment: false,
            }
        }
        case ADD_COMMENT_SUCCESS: {
            const postIndex = state.mainPosts.findIndex(v => v.id === action.data.postId);
            const post = state.mainPosts[postIndex];
            const Comments = [...post.Comments, dummyComment];
            console.log('before');
            const mainPosts = [...state.mainPosts];
            mainPosts[postIndex] = {...post, Comments};
            console.log('after');

            return {
                ...state,
                isAddingComment: false,
                addedComment: true,
                mainPosts,
            }
        }
        case ADD_COMMENT_FAILURE: {
            return {
                ...state,
                isAddingComment: false,
                addCommentErrorReason: action.error,
            }
        }

        default: {
            return {
                ...state
            }
        }
    }
};

export default reducer;
