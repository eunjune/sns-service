import React, { useCallback, useState } from 'react';
import PropTypes from 'prop-types'
import { Icon } from 'antd';
import ImagesZoom from './ImagesZoom';

const PostImages = ({images}) => {

  const [showImagesZoom, setShowImagesZoom] = useState(false);

  const onZoom = useCallback(() => {
    setShowImagesZoom(true);
  }, []);

  const onClose = useCallback(() => {
    setShowImagesZoom(false);
  }, []);

  if(images.length === 1) {
    return (
      <>
        <img src={`http://localhost:8080/image/${images[0]}`} alt="" onClick={onZoom}/>
        {showImagesZoom && <ImagesZoom images={images} onClose={onClose}/>}
      </>
    );
  }

  if(images.length === 2) {
    return (
      <>
        <div>
          <img src={`http://localhost:8080/image/${images[0]}`} width="50%" alt="" onClick={onZoom}/>
          <img src={`http://localhost:8080/image/${images[1]}`} width="50%" alt="" onClick={onZoom}/>
        </div>
        {showImagesZoom && <ImagesZoom images={images} onClose={onClose}/>}
      </>
    );
  }

  return (
    <>
      <div>
        <img src={`http://localhost:8080/image/${images[0]}`} width="50%" alt="" />
        <div style={{display: 'inline-block', width: '50%', textAlign: 'center', verticalAlign: 'middle'}} onClick={onZoom}>
          <Icon type='plus'/>
          <br/>
          {images.length - 1}
          개인 사진 더보기
        </div>
      </div>
      {showImagesZoom && <ImagesZoom images={images} onClose={onClose}/>}
    </>
  );
};

PostImages.propTypes = {
  images: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default PostImages;
