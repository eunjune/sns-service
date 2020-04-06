import React, { useState } from 'react';
import PropTypes from 'prop-types'
import {Overlay, Header, CloseButton, SlickWrapper, ImgWrapper, Indicator} from '../styles/imagesZoomStyle';
import Slick from 'react-slick';

const ImagesZoom = ({images,onClose}) => {
  const [currentSlide, setCurrentSlide] = useState(0);

  return (
    <Overlay>
      <Header>
        <h1>상세 이미지</h1>
        <CloseButton type="close" onClick={onClose}/>
      </Header>
      <SlickWrapper>
        <div>
          <Slick
            initialSlide={0}
            afterChange={(slide) => setCurrentSlide(slide)}
            infinite={false}
            arrows
            slidesToShow={1}
            slidesToScroll={1}
          >
            {images.map((v) => {
              const src = `http://localhost:8080/image/${v}`;
              return (
                <ImgWrapper>
                  <img src={src} alt=""/>
                </ImgWrapper>
              );
            })}
          </Slick>
          <Indicator style={{textAlign: 'center'}}>
            <div>
              {currentSlide + 1} / {images.length}
            </div>
          </Indicator>
        </div>
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
