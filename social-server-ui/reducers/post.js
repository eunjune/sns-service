import produce from 'immer'

export const initialState = {
    posts: [],
    imagePaths: [],
    editPostImages: [],
    editPostId:null,

    isAddingPost: false,
    addedPost: false,
    isAddingComment: false,
    addedComment: false,
    hasMorePost: false,
    isEditingPost: false,
    isEditPost: false,

    addCommentError: false,
    addCommentErrorReason: '',
    loadUserError: null,
    addPostError: false,
    addPostErrorReason: '',
};



export const LOAD_MAIN_POSTS_REQUEST = 'LOAD_MAIN_POSTS_REQUEST';
export const LOAD_MAIN_POSTS_SUCCESS = 'LOAD_MAIN_POSTS_SUCCESS';
export const LOAD_MAIN_POSTS_FAILURE = 'LOAD_MAIN_POSTS_FAILURE';

export const LOAD_MY_POSTS_REQUEST = 'LOAD_MY_POSTS_REQUEST';
export const LOAD_MY_POSTS_SUCCESS = 'LOAD_MY_POSTS_SUCCESS';
export const LOAD_MY_POSTS_FAILURE = 'LOAD_MY_POSTS_FAILURE';

export const LOAD_USER_POSTS_REQUEST = 'LOAD_USER_POSTS_REQUEST';
export const LOAD_USER_POSTS_SUCCESS = 'LOAD_USER_POSTS_SUCCESS';
export const LOAD_USER_POSTS_FAILURE = 'LOAD_USER_POSTS_FAILURE';

export const LOAD_HASHTAG_POSTS_REQUEST = 'LOAD_HASHTAG_POSTS_REQUEST';
export const LOAD_HASHTAG_POSTS_SUCCESS = 'LOAD_HASHTAG_POSTS_SUCCESS';
export const LOAD_HASHTAG_POSTS_FAILURE = 'LOAD_HASHTAG_POSTS_FAILURE';

export const LOAD_COMMENTS_REQUEST = 'LOAD_COMMENTS_REQUEST';
export const LOAD_COMMENTS_SUCCESS = 'LOAD_COMMENTS_SUCCESS';
export const LOAD_COMMENTS_FAILURE = 'LOAD_COMMENTS_FAILURE';

export const ADD_POST_REQUEST = 'ADD_POST_REQUEST';
export const ADD_POST_SUCCESS = 'ADD_POST_SUCCESS';
export const ADD_POST_FAILURE = 'ADD_POST_FAILURE';

export const UPLOAD_IMAGES_REQUEST = 'UPLOAD_IMAGES_REQUEST';
export const UPLOAD_IMAGES_SUCCESS = 'UPLOAD_IMAGES_SUCCESS';
export const UPLOAD_IMAGES_FAILURE = 'UPLOAD_IMAGES_FAILURE';

export const ADD_COMMENT_REQUEST = 'ADD_COMMENT_REQUEST';
export const ADD_COMMENT_SUCCESS = 'ADD_COMMENT_SUCCESS';
export const ADD_COMMENT_FAILURE = 'ADD_COMMENT_FAILURE';

export const RETWEET_REQUEST = 'RETWEET_REQUEST';
export const RETWEET_SUCCESS = 'RETWEET_SUCCESS';
export const RETWEET_FAILURE = 'RETWEET_FAILURE';

export const EDIT_POST_REQUEST = 'EDIT_POST_REQUEST';
export const EDIT_POST_SUCCESS = 'EDIT_POST_SUCCESS';
export const EDIT_POST_FAILURE = 'EDIT_POST_FAILURE';

export const LIKE_POST_REQUEST = 'LIKE_POST_REQUEST';
export const LIKE_POST_SUCCESS = 'LIKE_POST_SUCCESS';
export const LIKE_POST_FAILURE = 'LIKE_POST_FAILURE';

export const UNLIKE_POST_REQUEST = 'UNLIKE_POST_REQUEST';
export const UNLIKE_POST_SUCCESS = 'UNLIKE_POST_SUCCESS';
export const UNLIKE_POST_FAILURE = 'UNLIKE_POST_FAILURE';

export const REMOVE_POST_REQUEST = 'REMOVE_POST_REQUEST';
export const REMOVE_POST_SUCCESS = 'REMOVE_POST_SUCCESS';
export const REMOVE_POST_FAILURE = 'REMOVE_POST_FAILURE';

export const SHOW_EDIT_POST = 'SHOW_EDIT_POST';
export const CANCEL_EDIT_POST = 'CANCEL_EDIT_POST';
export const REMOVE_IMAGE = 'REMOVE_IMAGE';

export const SIZE = 4;

const reducer = (state = initialState, action) => {
    return produce(state, (draft) => {
        switch (action.type) {

    
            case LOAD_MAIN_POSTS_REQUEST: {
                console.log('my posts');
                draft.posts = action.lastId === 0 ? [] : draft.posts;
                draft.hasMorePost = action.lastId ? draft.hasMorePost : true;
                break;
            }

            case LOAD_MAIN_POSTS_SUCCESS: {
                draft.posts = draft.posts.concat(action.data);
                draft.hasMorePost = action.data.length === SIZE;
                break;
            }

            case LOAD_MAIN_POSTS_FAILURE: {
                break;
            }

            case LOAD_MY_POSTS_REQUEST: {

                draft.posts = action.data.lastId === 0 ? [] : draft.posts;
                draft.hasMorePost = action.data.lastId ? draft.hasMorePost : true;
                break;
            }

            case LOAD_MY_POSTS_SUCCESS: {
                draft.posts = draft.posts.concat(action.data);
                draft.hasMorePost = action.data.length === SIZE;
                break;
            }

            case LOAD_MY_POSTS_FAILURE: {
                break;
            }
    
            case LOAD_HASHTAG_POSTS_REQUEST: {
                draft.posts = !action.data.lastId ? [] : draft.posts;
                draft.hasMorePost = action.data.lastId ? draft.hasMorePost : true;
                break;
            }

            case LOAD_HASHTAG_POSTS_SUCCESS: {
                draft.posts = draft.posts.concat(action.data);
                draft.hasMorePost = action.data.length === SIZE;
                break;
            }

            case LOAD_HASHTAG_POSTS_FAILURE: {
                break;
            }

            case LOAD_USER_POSTS_REQUEST: {
                draft.posts = action.data.lastId === 0 ? [] : draft.posts;
                draft.hasMorePost = action.data.lastId ? draft.hasMorePost : true;
                break;
            }
    
            case LOAD_USER_POSTS_SUCCESS: {
                draft.posts = draft.posts.concat(action.data);
                draft.hasMorePost = action.data.length === 8;
                break;
            }

            case LOAD_USER_POSTS_FAILURE: {
                draft.loadUserError = action.error;
                break;
            }

            case LOAD_COMMENTS_REQUEST: {
                break;
            }

            case LOAD_COMMENTS_SUCCESS: {
                const postIndex = draft.posts.findIndex(p => p.id === action.data.postId);
                draft.posts[postIndex].comments = action.data.comments;
                break;
            }

            case LOAD_COMMENTS_FAILURE: {
                break;
            }

            case ADD_POST_REQUEST: {
                draft.isAddingPost =  true;
                draft.addedPost = false;
                draft.addPostErrorReason = '';
                break;
            }

            case ADD_POST_SUCCESS: {
                draft.isAddingPost = false;
                draft.addedPost = true;
                draft.posts.unshift(action.data);
                draft.addedPost = true;
                draft.imagePaths = [];
                break;
            }

            case ADD_POST_FAILURE: {
                draft.isAddingPost = false;
                draft.addPostError = true;
                draft.addPostErrorReason = action.error;
                break;
            }

            case UPLOAD_IMAGES_REQUEST: {
                break;
            }

            case UPLOAD_IMAGES_SUCCESS: {
                if(draft.isEditPost === true) {
                    action.data.forEach(v => {
                        draft.editPostImages.push(v)
                    });
                } else {
                    action.data.forEach(v => {
                        draft.imagePaths.push(v)
                    });
                }
                break;
            }

            case UPLOAD_IMAGES_FAILURE: {
                break;
            }

            case ADD_COMMENT_REQUEST: {
                draft.isAddingComment = true;
                draft.addCommentErrorReason = '';
                draft.addedComment = false;
                break;
            }

            case ADD_COMMENT_SUCCESS: {
                const postIndex = draft.posts.findIndex(p => p.id === action.data.postId);
                draft.posts[postIndex].comments.push(action.data.comment);
                ++draft.posts[postIndex].commentCount;
                draft.isAddingComment = false;
                draft.addedComment = true;
                break;
            }

            case ADD_COMMENT_FAILURE: {
                draft.isAddingComment = false;
                draft.addCommentErrorReason = action.error;
                break;
            }


            case RETWEET_REQUEST: {
                break;
            }

            case RETWEET_SUCCESS: {
                alert('리트윗 하셨습니다.');
                draft.posts.unshift(action.data);

                break;
            }
            case RETWEET_FAILURE: {
                alert(action.error.message);
                break;
            }

            case EDIT_POST_REQUEST: {
                console.log(action.data.postId);
                draft.isEditingPost = true;
                break;
            }

            case EDIT_POST_SUCCESS: {
                const postIndex = draft.posts.findIndex(p => p.id === action.data.id);
                draft.posts[postIndex] = action.data;
                draft.editPostImages = action.data.images;


                draft.isEditPost = false;
                draft.editPostId = null;
                draft.isEditingPost = false;

                break;
            }

            case EDIT_POST_FAILURE: {
                draft.isEditingPost = false;

                break;
            }

            case LIKE_POST_REQUEST: {
                break;
            }

            case LIKE_POST_SUCCESS: {
                const postIndex = draft.posts.findIndex(v => v.id === action.data.id);
                draft.posts[postIndex] = action.data;

                break;
            }

            case LIKE_POST_FAILURE: {
                break;
            }
    
            case REMOVE_POST_REQUEST: {
                break;
            }
    
            case REMOVE_POST_SUCCESS: {
                draft.posts = draft.posts.filter(v => v.id !== action.data);
                break;
            }

            case REMOVE_POST_FAILURE: {
                break;
            }

            case UNLIKE_POST_REQUEST: {
    
                break;
            }
            case UNLIKE_POST_SUCCESS: {
                const postIndex = draft.posts.findIndex(v => v.id === action.data.id);
                draft.posts[postIndex] = action.data;
                
                break;
            }

            case UNLIKE_POST_FAILURE: {
                break;
            }


            case SHOW_EDIT_POST: {
                draft.isEditPost = true;
                draft.editPostId = action.postId;
                draft.editPostImages = action.images;
                break;
            }

            case CANCEL_EDIT_POST: {
                draft.isEditPost = false;
                draft.editPostId = null;
                break;
            }

            case REMOVE_IMAGE: {
                if(draft.isEditPost === true) {
                    const index = draft.editPostImages.findIndex((v,i) => i === action.index);
                    draft.editPostImages.splice(index,1);
                } else {
                    const index = draft.imagePaths.findIndex((v,i) => i === action.index);
                    draft.imagePaths.splice(index,1);
                }
                break;
            }

            default: {
                break;
            }

        }
    });

    
};

export default reducer;
