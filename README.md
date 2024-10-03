# 퍼줄게 (Peojulgae)

퍼줄게는 소상공인을 지원하고 지역 사회의 경제 활성화를 목표로 개발된 음식 결제 애플리케이션입니다. 사용자는 지역 음식점에 대한 정보를 확인하고 할인된 가격으로 음식을 주문함으로써 경제적인 혜택을 누릴 수 있으며, 소상공인들은 매출을 증대시키고 운영에 필요한 부담을 줄일 수 있습니다.

## 문제 인식
현재 물가 상승과 같은 여러 요인으로 인해 경제 활동이 침체되고 있으며, 소상공인들은 음식점을 운영하는 과정에서 많은 고정 비용과 감소한 매출로 인해 경영에 어려움을 겪고 있습니다. 이러한 경제 및 사회적 문제를 해결하기 위해, 퍼줄게는 소상공인과 소비자 모두에게 이익을 제공하는 음식 결제 솔루션을 제공합니다.

## 프로젝트 목표
퍼줄게는 사용자가 음식점 정보를 확인하고 합리적인 가격에 음식을 구매할 수 있도록 지원합니다. 이를 통해 지역 경제를 활성화하고 소상공인들의 경영을 돕는 것이 목표입니다. 사용자는 할인된 가격에 음식을 해결함으로써 경제적 안정성을 높일 수 있으며, 소상공인은 매출 증대를 기대할 수 있습니다.

## 주요 기능
- **주변 음식점 확인**: KakaoMap API를 활용하여 사용자의 GPS 위치를 기반으로 주변 음식점을 검색할 수 있습니다.
- **결제 기능**: BootPay API를 통해 간편하고 안전한 결제 시스템을 제공합니다.
- **실시간 데이터베이스**: Firebase Real-Time-Database를 이용하여 실시간으로 데이터를 저장하고 관리합니다.

## 기술 스택
- **백엔드**: Firebase Real-Time-Database
- **프론트엔드**: Android (Java), KakaoMap API, BootPay API
- **결제 시스템**: BootPay API
- **지도 및 위치 서비스**: KakaoMap API
- **실시간 데이터 처리**: Firebase Real-Time-Database

## 기대 효과
- 사용자는 할인된 가격으로 음식을 구매하여 경제적 부담을 줄일 수 있습니다.
- 소상공인은 매출을 증대시키고 경영에 필요한 고정 비용을 줄일 수 있습니다.
- 지역 사회의 경제 활동을 촉진하여 활력을 되찾을 수 있습니다.

## 프로젝트 구조
```plaintext
📦 퍼줄게
 ┣ 📂 app
 ┃ ┣ 📂 src
 ┃ ┃ ┣ 📂 main
 ┣ 📄 README.md
 ┣ 📄 AndroidManifest.xml
 ┗ 📄 build.gradle


설치 및 실행 방법

	1.	KakaoMap API Key를 발급받아 AndroidManifest.xml에 추가하세요.
	2.	BootPay API Key를 발급받아 결제 기능을 설정하세요.
	3.	Firebase 프로젝트를 생성하고 google-services.json 파일을 Android 프로젝트에 추가하세요.





# English Ver.
# Peojulgae

Peojulgae is a food payment application developed with the goal of supporting small business owners and revitalizing the local economy. Users can browse information about local restaurants and order food at discounted prices, providing financial benefits to consumers while helping small business owners increase their sales and reduce operational burdens.

## Problem Recognition
Due to factors like rising inflation and economic stagnation, small business owners are facing difficulties with the high fixed costs of running a restaurant and decreasing sales. To address these economic and social challenges, Peojulgae offers a solution that benefits both small businesses and consumers by providing a convenient food payment platform.

## Project Objective
Peojulgae aims to help users find restaurant information and purchase meals at reasonable prices. This promotes local economic activity and supports the management of small businesses. By offering discounted meals, users can increase their financial stability, while small business owners can boost their sales.

## Key Features
- **Find Nearby Restaurants**: Leverages the KakaoMap API to display nearby restaurants based on the user's GPS location.
- **Payment System**: Integrates the BootPay API for a simple and secure payment experience.
- **Real-time Database**: Utilizes Firebase Real-Time-Database to store and manage data in real-time.

## Tech Stack
- **Backend**: Firebase Real-Time-Database
- **Frontend**: Android (Java), KakaoMap API, BootPay API
- **Payment System**: BootPay API
- **Map and Location Services**: KakaoMap API
- **Real-time Data Handling**: Firebase Real-Time-Database

## Expected Benefits
- **For Users**: Reduce expenses by purchasing meals at discounted prices.
- **For Small Business Owners**: Increase sales and reduce operational costs.
- **For the Local Community**: Promote economic activities and reinvigorate the local economy.

## Project Structure
```plaintext
📦 Peojulgae
 ┣ 📂 app
 ┃ ┣ 📂 src
 ┃ ┃ ┣ 📂 main
 ┃ ┃ ┣ 📂 test
 ┣ 📄 README.md
 ┣ 📄 AndroidManifest.xml
 ┗ 📄 build.gradle


# 프로젝트 클론
git clone https://github.com/Minu0803/Peojulgae.git

# Android Studio에서 프로젝트 열기
# 빌드 및 실행
