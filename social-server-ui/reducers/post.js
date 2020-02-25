export const initialState = {
    mainPosts: [{
        User: {
            id: 1,
            name: '이름'
        },
        content: '첫번째 게시글',
        img: '',
        createdAt: {},
    }],
    imagePaths: [],
};

const ADD_POST = 'ADD_POST';
const ADD_DUMMY = 'ADD_DUMMY';

const addPost = {
    type: ADD_POST,
};

const addDummy = {
    type: ADD_DUMMY,
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
        case ADD_POST: {
            return {
                ...state,
            }
        }
        case ADD_DUMMY: {
            return {
                ...state,
                mainPosts: [action,data, ...state.mainPosts],
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