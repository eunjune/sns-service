import React, { useState } from 'react';
import PropTypes from 'prop-types'
import {Overlay, Header, CloseButton, SlickWrapper, ImgWrapper, Indicator} from '../styles/imagesZoomStyle';
import Slick from 'react-slick';
import {Carousel} from "antd";
import {baseUrl} from "../../saga";

const ImagesZoom = ({images,onClose}) => {
  const [currentSlide, setCurrentSlide] = useState(0);

  return (
    <Overlay>
      <Header>
        <h1>상세 이미지</h1>
        <CloseButton type="close" onClick={onClose}/>
      </Header>
      <SlickWrapper>
          <Carousel>
              {images.map((v) => {
                  const src = baseUrl + `image/${v}`;
                  return (
                      <ImgWrapper>
                          <img src={src} alt=""/>
                      </ImgWrapper>
                  );
              })}
          </Carousel>
      </SlickWrapper>
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
