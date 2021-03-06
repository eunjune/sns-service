import produce from 'immer'

export const initialState = {
    post: {},
    posts: [],
    imagePaths: [],
    editPostImages: [],
    editPostId: null,

    isAddingPost: false,
    addedPost: false,
    isAddingComment: false,
    addedComment: false,
    hasMorePost: false,
    isEditingPost: false,
    isEditPost: false,

    retweetError: null,
    addCommentError: null,
    loadUserPostError: null,
    addPostError: null,
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

export const LOAD_SEARCH_POSTS_REQUEST = 'LOAD_SEARCH_POSTS_REQUEST';
export const LOAD_SEARCH_POSTS_SUCCESS = 'LOAD_SEARCH_POSTS_SUCCESS';
export const LOAD_SEARCH_POSTS_FAILURE = 'LOAD_SEARCH_POSTS_FAILURE';

export const LOAD_COMMENTS_REQUEST = 'LOAD_COMMENTS_REQUEST';
export const LOAD_COMMENTS_SUCCESS = 'LOAD_COMMENTS_SUCCESS';
export const LOAD_COMMENTS_FAILURE = 'LOAD_COMMENTS_FAILURE';

export const LOAD_POST_REQUEST = 'LOAD_POST_REQUEST';
export const LOAD_POST_SUCCESS = 'LOAD_POST_SUCCESS';
export const LOAD_POST_FAILURE = 'LOAD_POST_FAILURE';

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

export const LOG_OUT_POST = 'LOG_OUT_POST';

export const SIZE = 3;

const reducer = (state = initialState, action) => {
    return produce(state, (draft) => {
        switch (action.type) {
            case LOAD_MAIN_POSTS_REQUEST: {
                draft.posts = action.data.lastId === 0 ? [] : draft.posts;
                draft.hasMorePost = action.data.lastId ? draft.hasMorePost : true;
                break;
            }

            case LOAD_MAIN_POSTS_SUCCESS: {
                draft.posts = draft.posts.concat(action.data);
                draft.hasMorePost = action.data.length === SIZE;
                break;
            }

            case LOAD_MAIN_POSTS_FAILURE: {
                console.error(action.error);

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
                console.log(draft.posts.length);
                break;
            }

            case LOAD_MY_POSTS_FAILURE: {
                console.error(action.error);

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
                console.error(action.error);

                break;
            }

            case LOAD_SEARCH_POSTS_REQUEST: {
                draft.posts = !action.data.lastId ? [] : draft.posts;
                draft.hasMorePost = action.data.lastId ? draft.hasMorePost : true;
                break;
            }

            case LOAD_SEARCH_POSTS_SUCCESS: {
                draft.posts = draft.posts.concat(action.data);
                draft.hasMorePost = action.data.length === SIZE;
                break;
            }

            case LOAD_SEARCH_POSTS_FAILURE: {
                console.error(action.error);

                break;
            }

            case LOAD_USER_POSTS_REQUEST: {
                console.log(action.data.lastId ? draft.hasMorePost : true);
                console.log(LOAD_USER_POSTS_REQUEST);
                draft.posts = !action.data.lastId ? [] : draft.posts;
                draft.hasMorePost = action.data.lastId ? draft.hasMorePost : true;
                break;
            }

            case LOAD_USER_POSTS_SUCCESS: {
                draft.posts = draft.posts.concat(action.data);
                draft.hasMorePost = action.data.length === SIZE;
                break;
            }

            case LOAD_USER_POSTS_FAILURE: {
                console.error(action.error);

                draft.loadUserPostError = action.error;
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
                console.error(action.error);

                break;
            }

            case LOAD_POST_REQUEST: {
                break;
            }

            case LOAD_POST_SUCCESS: {
                draft.post = action.data
                break;
            }

            case LOAD_POST_FAILURE: {
                console.error(action.error);
                break;
            }

            case ADD_POST_REQUEST: {
                draft.isAddingPost = true;
                draft.addedPost = false;
                draft.addPostError = null;
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
                console.error(action.error);

                draft.isAddingPost = false;
                draft.addPostError = action.error;
                break;
            }

            case UPLOAD_IMAGES_REQUEST: {
                break;
            }

            case UPLOAD_IMAGES_SUCCESS: {
                if (draft.isEditPost === true) {
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
                console.error(action.error);

                break;
            }

            case ADD_COMMENT_REQUEST: {
                draft.isAddingComment = true;
                draft.addCommentError = null;
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
                console.error(action.error);

                draft.isAddingComment = false;
                draft.addCommentError = action.error;
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
                console.error(action.error);
                draft.retweetError = action.error;
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
                console.error(action.error);

                draft.isEditingPost = false;

                break;
            }

            case LIKE_POST_REQUEST: {
                break;
            }

            case LIKE_POST_SUCCESS: {
                const postIndex = draft.posts.findIndex(v => v.id === action.data.id);
                draft.posts[postIndex].likeCount = action.data.likeCount;
                draft.posts[postIndex].likesOfMe = action.data.likesOfMe;

                break;
            }

            case LIKE_POST_FAILURE: {
                console.error(action.error);

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
                console.error(action.error);

                break;
            }

            case UNLIKE_POST_REQUEST: {

                break;
            }
            case UNLIKE_POST_SUCCESS: {
                const postIndex = draft.posts.findIndex(v => v.id === action.data.id);
                draft.posts[postIndex].likeCount = action.data.likeCount;
                draft.posts[postIndex].likesOfMe = action.data.likesOfMe;

                break;
            }

            case UNLIKE_POST_FAILURE: {
                console.error(action.error);

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
                if (draft.isEditPost === true) {
                    const index = draft.editPostImages.findIndex((v, i) => i === action.index);
                    draft.editPostImages.splice(index, 1);
                } else {
                    const index = draft.imagePaths.findIndex((v, i) => i === action.index);
                    draft.imagePaths.splice(index, 1);
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
