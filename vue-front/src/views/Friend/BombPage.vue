<template>
    <section class="creative-carousal--hero">
      <div class="carousel-slider swiper-container-horizontal">
        <div class="swiper-wrapper">
          <div
            class="swiper-slide"
            v-for="(slide, index) in slides"
            :key="index"
            :style="{ backgroundImage: `url(${slide.url})` }"
          >
            <div class="inner">
              <h2>{{ slide.title }}</h2>
              <a href="#">{{ slide.linkText }}</a>
            </div>
          </div>
        </div>
        <!-- End Of Swiper -->
        <div class="slide-progress">
          <span>01</span>
          <div class="swiper-pagination swiper-pagination-progressbar">
            <span class="swiper-pagination-progressbar-fill"></span>
          </div>
          <span>05</span>
        </div>
        <!-- end of progress -->
        <div class="swiper-button-prev">PREV</div>
        <!-- end button-prev -->
        <div class="swiper-button-next">NEXT</div>
        <!-- end button-next -->
      </div>
    </section>
  </template>
  
  <script setup>
  import { onMounted } from 'vue';
  import $ from 'jquery';
  import 'swiper/css';
  import Swiper from 'swiper';
  
  const slides = [
    {
      url: 'https://imgpanda.com/upload/ib/YCgMd74kJD.jpg',
      title: 'DISCOVER',
      linkText: 'DISCOVER CASE'
    },
    {
      url: 'https://imgpanda.com/upload/ib/lcLiwDdGNI.jpg',
      title: 'EXCLUSION',
      linkText: 'DISCOVER CASE'
    },
    {
      url: 'https://imgpanda.com/upload/ib/b20k1l8dNs.jpg',
      title: 'EDITIONAL',
      linkText: 'DISCOVER CASE'
    },
    {
      url: 'https://imgpanda.com/upload/ib/VHrpwdiZAn.jpg',
      title: 'AVAILABLE',
      linkText: 'DISCOVER CASE'
    },
    {
      url: 'https://imgpanda.com/upload/ib/0XEWL9H9RV.jpg',
      title: 'PREMIUM',
      linkText: 'DISCOVER CASE'
    }
  ];
  
  onMounted(() => {
    $(document).ready(function () {
      var carouselslider = new Swiper(".carousel-slider", {
        spaceBetween: 0,
        slidesPerView: 3,
        centeredSlides: true,
        autoplay: {
          delay: 9500,
          disableOnInteraction: false
        },
        navigation: {
          nextEl: ".swiper-button-next",
          prevEl: ".swiper-button-prev"
        },
        pagination: {
          el: ".swiper-pagination",
          type: "progressbar"
        },
        loop: true,
        breakpoints: {
          1024: {
            slidesPerView: 3
          },
          768: {
            slidesPerView: 2
          },
          640: {
            slidesPerView: 1
          },
          320: {
            slidesPerView: 1
          }
        }
      });
    });
  });
  </script>
  
<style scoped>
@import '@/assets/personalShop.css';
</style>
  