import React, { useState } from 'react';
import PropTypes from 'prop-types'
import { Icon } from 'antd';
import styled from 'styled-components';

const Overlay = styled.div`
  position: fixed;
  z-index: 5000;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
`;

const Header = styled.header`
  height: 44px;
  background: white;
  position: relative;
  padding: 0;
  text-align: center;

  & h1 {
    margin: 0;
    font-size: 17px;
    color: #333;
    line-height: 44px;
  }
`;

const H1 = styled.h1`
`;

const SlickWrapper = styled.div`
  height: calc(100% - 44px);
  background: #090909;
`;

const CloseButton= styled(Icon)`
  position: absolute;
  right: 0;
  top: 0;
  padding: 15;
  line-height: 14px;
  cursor: pointer;
`

const Indicator = styled.div`
  text-align: 'center';

  & > div {
    width: 75px;
    height: 30px;
    line-height: 30px;
    border-radius: 15px;
    background: #313131;
    display: inline-block;
    text-align: center;
    color: white;
    font-size: 15px;
  }
`;

const ImgWrapper = styled.div`
  padding: 32px;
  text-align: center;

  & img {
    margin: '0 auto';
    max-height: 750px;
  }
`;

const ImagesZoom = ({images,onClose}) => {
  const [currentSlide, setCurrentSlide] = useState(0);

  console.log(images);

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
              const src = `http://localhost:8080/image/${v.path}`;
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
