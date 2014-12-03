#!/bin/sh

#  build.sh
#  SDKCore
#
#  Created by wu wei on 2014/11/27.
#  Copyright (c) 2014年 SDG. All rights reserved.
#!/bin/sh

#SDK= ビルドに使用するSDK
#CONFIGURATION= Debug か Release
#WORKSPACE_FILE= ワークスペースのパス
## PROJECT_FILE=プロジェクトファイルを指定する場合はこのパス
#TARGET_NAME=ターゲット名
#SCHEME_NAME=スキーム名
#PRODUCT_NAME=プロダクト名
#IPA_FILE_NAME=IPAファイル
#OUT_APP_DIR=.appを出力するフォルダ
#OUT_IPA_DIR=.ipaを出力するフォルダ
#DISTRIBUTION_CERTIFICATE=証明書の名前（キーチェーンから参照）
#PROVISIONING_PATH=プロビジョニングファイル（一度XcodeでArchiveしたときに参照できます。）

SDK="iphoneos"
CONFIGURATION="Release"
WORKSPACE_FILE="../WORKSPACE.xcworkspace"
PROJECT_FILE="PROJECT.xcodeproj"
TARGET_NAME="TARGET"
SCHEME_NAME="SCHEME"
PRODUCT_NAME="PROJECT"
#IPA_FILE_NAME="sugedo"
OUT_APP_DIR="out"
#OUT_IPA_DIR="out"
#DISTRIBUTION_CERTIFICATE="iPhone Distribution: Company Co.,Ltd."
#PROVISIONING_PATH="${HOME}/Library/MobileDevice/Provisioning Profiles/7aab1f2a-d2ec-4f07-8dc0-0b33fbfb9628.mobileprovision"


#if [ ! -d ${OUT_IPA_DIR} ]; then
#mkdir "${OUT_IPA_DIR}"
#fi

## when using xcworkspace
# WORKSPACE_File="SampleApp.xcworkspace"
# xcodebuild clean -workspace "${WORKSPACE_FILE}" -scheme "${SCHEME_NAME}"
# xcodebuild -workspace "${WORKSPACE_FILE}" -scheme "${SCHEME_NAME}" -sdk "${SDK}" -configuration "${CONFIGURATION}" install DSTROOT="${OUT_APP_DIR}"

echo "-- start xcodebuild clean"
sleep 1
#xcodebuild clean -workspace "${WORKSPACE_FILE}" -scheme "${SCHEME_NAME}"
xcodebuild clean -workspace "${WORKSPACE_FILE}" -scheme "${SCHEME_NAME}"
echo "complete xcodebuild clean"
sleep 1

echo "-- start xcodebuild"
sleep 1
#xcodebuild -workspace "${WORKSPACE_FILE}" -scheme "${SCHEME_NAME}" -sdk "${SDK}" -configuration "${CONFIGURATION}" install DSTROOT="${OUT_APP_DIR}"
xcodebuild -workspace "${WORKSPACE_FILE}" -scheme "${SCHEME_NAME}" -sdk "${SDK}"

echo "-- complete xcodebuild"
sleep 1


#echo "-- start xcrun"
#sleep 1
#xcrun -sdk "${SDK}" PackageApplication "${PWD}/${OUT_APP_DIR}/Applications/${PRODUCT_NAME}.app" -o "${PWD}/${OUT_IPA_DIR}/${IPA_FILE_NAME}.ipa" --sign "${DISTRIBUTION_CERTIFICATE}" -embed "${PROVISIONING_PATH}"
#echo "-- complete xcrun"
#sleep 1