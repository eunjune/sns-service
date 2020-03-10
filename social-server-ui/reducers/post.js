export const initialState = {
    posts: [],
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

export const REMOVE_POST_REQUEST = 'REMOVE_POST_REQUEST';
export const REMOVE_POST_SUCCESS = 'REMOVE_POST_SUCCESS';
export const REMOVE_POST_FAILURE = 'REMOVE_POST_FAILURE';

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

export const LIKE_POST_REQUEST = 'LIKE_POST_REQUEST';
export const LIKE_POST_SUCCESS = 'LIKE_POST_SUCCESS';
export const LIKE_POST_FAILURE = 'LIKE_POST_FAILURE';

export const UNLIKE_POST_REQUEST = 'UNLIKE_POST_REQUEST';
export const UNLIKE_POST_SUCCESS = 'UNLIKE_POST_SUCCESS';
export const UNLIKE_POST_FAILURE = 'UNLIKE_POST_FAILURE';


const reducer = (state = initialState, action) => {
    switch (action.type) {
        case ADD_POST_REQUEST: {
            return {
                ...state,
                isAddingPost: true,
                addedPost: false,
            }
        }
        case ADD_POST_SUCCESS: {
            return {
                ...state,
                isAddingPost: false,
                addedPost: true,
                posts: [action.data, ...state.posts],
                imagePaths: [],
            }
        }
        case ADD_POST_FAILURE: {
            return {
                ...state,
                isAddingPost: false,
                addPostError: true,
                addPostErrorReason: action.error,
            }
        }



        case LOAD_MAIN_POSTS_REQUEST: {
            return {
                ...state,
                posts: []
            }
        }
        case LOAD_MAIN_POSTS_SUCCESS: {
            return {
                ...state,
                posts: action.data,
            }
        }
        case LOAD_MAIN_POSTS_FAILURE: {
            return {
                ...state,
            }
        }

        case LOAD_HASHTAG_POSTS_REQUEST: {
            return {
                ...state,
                posts: []
            }
        }
        case LOAD_HASHTAG_POSTS_SUCCESS: {
            return {
                ...state,
                posts: action.data,
            }
        }
        case LOAD_HASHTAG_POSTS_FAILURE: {
            return {
                ...state,
            }
        }
        case LOAD_USER_POSTS_REQUEST: {
            return {
                ...state,
                posts: []
            }
        }

        case LOAD_USER_POSTS_SUCCESS: {
            return {
                ...state,
                posts: action.data,
            }
        }
        case LOAD_USER_POSTS_FAILURE: {
            return {
                ...state,
            }
        }

        case REMOVE_POST_REQUEST: {
            return {
                ...state,
            }
        }

        case REMOVE_POST_SUCCESS: {
            console.log(action.data);
            return {
                ...state,
                posts: state.posts.filter(v => v.id !== action.data),
            }
        }
        case REMOVE_POST_FAILURE: {
            return {
                ...state,
            }
        }

        case LOAD_COMMENTS_REQUEST: {
            return {
                ...state,
            }
        }

        case LOAD_COMMENTS_SUCCESS: {
            const postIndex = state.posts.findIndex(p => p.id === action.data.postId);
            const post = state.posts[postIndex];
            const comments = action.data.comments;
            const posts = [...state.posts];
            posts[postIndex] = {...post, comments: comments};

            return {
                ...state,
                posts: posts,
            }
        }
        case LOAD_COMMENTS_FAILURE: {
            return {
                ...state,
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
            const postIndex = state.posts.findIndex(p => p.id === action.data.postId);

            const post = state.posts[postIndex];
            const comments = [...post.comments, action.data.comment];
            const posts = [...state.posts];
            posts[postIndex] = {...post, comments: comments};

            return {
                ...state,
                isAddingComment: false,
                addedComment: true,
                posts: posts,
            }
        }
        case ADD_COMMENT_FAILURE: {
            return {
                ...state,
                isAddingComment: false,
                addCommentErrorReason: action.error,
            }
        }

        case UPLOAD_IMAGES_REQUEST: {

            return {
                ...state,

            }
        }
        case UPLOAD_IMAGES_SUCCESS: {
            return {
                ...state,
                imagePaths: [...state.imagePaths, ...action.data],
            }
        }
        case UPLOAD_IMAGES_FAILURE: {
            return {
                ...state,
            }
        }

        case REMOVE_IMAGE: {
            return {
                ...state,
                imagePaths: state.imagePaths.filter((v,i) => i !== action.index),
            }
        }

        case LIKE_POST_REQUEST: {

            return {
                ...state,

            }
        }
        case LIKE_POST_SUCCESS: {
            const postIndex = state.posts.findIndex(v => v.id === action.data.id);
            const newPosts = [...state.posts];
            newPosts[postIndex] = action.data;
            
            return {
                ...state,
                posts : newPosts,
            }
        }
        case LIKE_POST_FAILURE: {
            return {
                ...state,
            }
        }

        case UNLIKE_POST_REQUEST: {

            return {
                ...state,

            }
        }
        case UNLIKE_POST_SUCCESS: {
            const postIndex = state.posts.findIndex(v => v.id === action.data.id);
            const newPosts = [...state.posts];
            newPosts[postIndex] = action.data;
            
            return {
                ...state,
                posts : newPosts,
            }
        }
        case UNLIKE_POST_FAILURE: {
            return {
                ...state,
            }
        }

        case RETWEET_REQUEST: {

            return {
                ...state,

            }
        }
        case RETWEET_SUCCESS: {
            
            return {
                ...state,
                posts : [action.data, ...state.posts],
            }
        }
        case RETWEET_FAILURE: {
            return {
                ...state,
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
