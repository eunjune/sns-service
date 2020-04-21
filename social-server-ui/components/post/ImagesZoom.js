import React from 'react';
import PropTypes from 'prop-types'
import {Overlay, Header, CloseButton, ImgWrapper} from '../styles/imagesZoomStyle';
import {Carousel} from "antd";

const ImagesZoom = ({images, onClose}) => {

    return (
        <Overlay>
            <Header>
                <CloseButton type="close" onClick={onClose}/>
                <h1>상세 이미지</h1>
            </Header>
            <Carousel>
                {images.map((v) => {
                    const src = `${v}`;
                    return (
                        <ImgWrapper>
                            <img src={src} alt=""/>
                        </ImgWrapper>
                    );
                })}
            </Carousel>

        </Overlay>
    );
};

ImagesZoom.propTypes = {
    images: PropTypes.arrayOf(PropTypes.shape({
        path: PropTypes.string,
    })).isRequired,
    onClose: PropTypes.func.isRequired,
};

export default ImagesZoom;
