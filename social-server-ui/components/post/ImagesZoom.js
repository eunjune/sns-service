import React, { useState } from 'react';
import PropTypes from 'prop-types'
import {Overlay, Header, CloseButton, SlickWrapper, ImgWrapper, Indicator} from '../styles/imagesZoomStyle';
import Slick from 'react-slick';
import {Carousel} from "antd";
import {baseUrl} from "../../config/config";

const ImagesZoom = ({images,onClose}) => {

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
