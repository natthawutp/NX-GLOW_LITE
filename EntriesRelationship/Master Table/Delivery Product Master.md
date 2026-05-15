# GWH_TM_DVPD
#Entity #Standard #MASTER

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(200) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(200) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | DVPD_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | DVPD_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | DVPD_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | DVPD_DLV_COD | NVARCHAR2(80) | N | Delivery To Code / 配送先コード |
| 22 | DVPD_PRDG_COD | NVARCHAR2(30) | N | Product Group Code / 商品グループコード |
| 23 | DVPD_PROD_COD | NVARCHAR2(100) | N | Product Code / 商品コード |
| 24 | DVPD_PIK1_PRPB_FLG | CHAR(1) | N | PIK1 Plural Prohibition Flag / PIK1複数ﾋﾟｯｸｷｰ禁止フラグ |
| 25 | DVPD_PIK2_PRPB_FLG | CHAR(1) | N | PIK2 Plural Prohibition Flag / PIK2複数ﾋﾟｯｸｷｰ禁止フラグ |
| 26 | DVPD_PIK3_PRPB_FLG | CHAR(1) | N | PIK3 Plural Prohibition Flag / PIK3複数ﾋﾟｯｸｷｰ禁止フラグ |
| 27 | DVPD_PIK4_PRPB_FLG | CHAR(1) | N | PIK4 Plural Prohibition Flag / PIK4複数ﾋﾟｯｸｷｰ禁止フラグ |
| 28 | DVPD_PIK5_PRPB_FLG | CHAR(1) | N | PIK5 Plural Prohibition Flag / PIK5複数ﾋﾟｯｸｷｰ禁止フラグ |
| 29 | DVPD_CLI1 | NVARCHAR2(200) | Y | Used for only customization01 / 顧客専用項目01 |
| 30 | DVPD_CLI2 | NVARCHAR2(200) | Y | Used for only customization02 / 顧客専用項目02 |
| 31 | DVPD_CLI3 | NVARCHAR2(200) | Y | Used for only customization03 / 顧客専用項目03 |
| 32 | DVPD_CLI4 | NVARCHAR2(200) | Y | Used for only customization04 / 顧客専用項目04 |
| 33 | DVPD_CLI5 | NVARCHAR2(200) | Y | Used for only customization05 / 顧客専用項目05 |
| 34 | DVPD_CLI6 | NVARCHAR2(200) | Y | Used for only customization06 / 顧客専用項目06 |
| 35 | DVPD_CLI7 | NVARCHAR2(200) | Y | Used for only customization07 / 顧客専用項目07 |
| 36 | DVPD_CLI8 | NVARCHAR2(200) | Y | Used for only customization08 / 顧客専用項目08 |
| 37 | DVPD_CLI9 | NVARCHAR2(200) | Y | Used for only customization09 / 顧客専用項目09 |
| 38 | DVPD_CLI10 | NVARCHAR2(200) | Y | Used for only customization10 / 顧客専用項目10 |
| 39 | DVPD_COI1 | NVARCHAR2(200) | Y | Customized Only Item 01 / カスタマイズ項目01 |
| 40 | DVPD_COI2 | NVARCHAR2(200) | Y | Customized Only Item 02 / カスタマイズ項目02 |
| 41 | DVPD_COI3 | NVARCHAR2(200) | Y | Customized Only Item 03 / カスタマイズ項目03 |
| 42 | DVPD_COI4 | NVARCHAR2(200) | Y | Customized Only Item 04 / カスタマイズ項目04 |
| 43 | DVPD_COI5 | NVARCHAR2(200) | Y | Customized Only Item 05 / カスタマイズ項目05 |
| 44 | DVPD_COI6 | NVARCHAR2(200) | Y | Customized Only Item 06 / カスタマイズ項目06 |
| 45 | DVPD_COI7 | NVARCHAR2(200) | Y | Customized Only Item 07 / カスタマイズ項目07 |
| 46 | DVPD_COI8 | NVARCHAR2(200) | Y | Customized Only Item 08 / カスタマイズ項目08 |
| 47 | DVPD_COI9 | NVARCHAR2(200) | Y | Customized Only Item 09 / カスタマイズ項目09 |
| 48 | DVPD_COI10 | NVARCHAR2(200) | Y | Customized Only Item 10 / カスタマイズ項目10 |
| 49 | DVPD_PRC_BOX_FEE | NUMBER(14,4) | Y | Price Box Fee / 箱開け単価 |
| 50 | DVPD_PRC_DIV_FEE | NUMBER(14,4) | Y | Price Division Fee / 分割単価 |
