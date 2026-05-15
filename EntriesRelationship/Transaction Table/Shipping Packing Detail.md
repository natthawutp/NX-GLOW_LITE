# GWH_TJ_SP_PD
#Entity #Standard #OUTBOUND 

| COLUMN_NAME    | DATA_TYPE           | NULLABLE | DATA_DEFAULT | COLUMN_ID | COMMENTS                                    |
|----------------|---------------------|----------|--------------|-----------|---------------------------------------------|
| DEL_FLG        | CHAR(1 BYTE)        | No       |              | 1         | Delete Flag / 論理削除フラグ                       |
| CRT_YMD        | CHAR(8 BYTE)        | No       |              | 2         | Create Date (UTC) / 作成日(標準時)                |
| CRT_TIM        | CHAR(6 BYTE)        | No       |              | 3         | Create HMS (UTC) / 作成時分秒(標準時)               |
| CRT_TMID       | NVARCHAR2(100 CHAR) | No       |              | 4         | Create Term Id / 作成端末ID                     |
| CRT_USER       | NVARCHAR2(50 CHAR)  | No       |              | 5         | Create User Id / 作成ユーザID                    |
| CRT_PGM        | NVARCHAR2(30 CHAR)  | No       |              | 6         | Create Program Id / 作成プログラムID               |
| CRT_TM_ZONE    | NVARCHAR2(3 CHAR)   | No       |              | 7         | Create User Time Zone / 作成ユーザタイムゾーン         |
| CRT_YMDHMS     | TIMESTAMP(6)        | No       |              | 8         | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時）    |
| CRT_L_YMDHMS   | TIMESTAMP(6)        | No       |              | 9         | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| UPD_YMD        | CHAR(8 BYTE)        | No       |              | 10        | Update Date (UTC) / 更新日(標準時)                |
| UPD_TIM        | CHAR(6 BYTE)        | No       |              | 11        | Update HMS (UTC) / 更新時分秒(標準時)               |
| UPD_TMID       | NVARCHAR2(100 CHAR) | No       |              | 12        | Update Terminal ID / 更新端末ID                 |
| UPD_USER       | NVARCHAR2(50 CHAR)  | No       |              | 13        | Update User ID / 更新ユーザID                    |
| UPD_PGM        | NVARCHAR2(30 CHAR)  | No       |              | 14        | Update Program ID / 更新プログラムID               |
| UPD_TM_ZONE    | NVARCHAR2(3 CHAR)   | No       |              | 15        | Update User Time Zone / 更新ユーザタイムゾーン         |
| UPD_YMDHMS     | TIMESTAMP(6)        | No       |              | 16        | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時）    |
| UPD_L_YMDHMS   | TIMESTAMP(6)        | No       |              | 17        | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| SPPD_CPNY_COD  | NVARCHAR2(6 CHAR)   | No       |              | 18        | Company Code / カンパニーコード                     |
| SPPD_WHS_COD   | NVARCHAR2(4 CHAR)   | No       |              | 19        | Warehouse Code / 倉庫コード                      |
| SPPD_CUST_COD  | NVARCHAR2(13 CHAR)  | No       |              | 20        | Customer Code / 顧客コード                       |
| SPPD_PC_NUM    | NVARCHAR2(6 CHAR)   | No       |              | 21        | Packing Number / 梱包番号                       |
| SPPD_PCLN_NUM  | NUMBER(4,0)         | No       |              | 22        | Packing Line Number / 梱包ラインNo               |
| SPPD_SP_NUM    | NVARCHAR2(6 CHAR)   | No       |              | 23        | Shipping Number / 出荷番号                      |
| SPPD_SPLN_NUM  | NUMBER(4,0)         | No       |              | 24        | Shipping Line Number / 出荷ラインNo              |
| SPPD_SPSQ_NUM  | NUMBER(4,0)         | No       |              | 25        | Shipping Seq Number / 出荷SEQNo               |
| SPPD_PROD_COD  | NVARCHAR2(50 CHAR)  | No       |              | 26        | Product Code / 商品コード                        |
| SPPD_ORGN_COD  | CHAR(2 BYTE)        | Yes      |              | 27        | Country Of Origin / 原産国                     |
| SPPD_RCS_QTY   | NUMBER(9,0)         | No       |              | 28        | Case QTY(Result.) / ケース実績数                  |
| SPPD_RPC_QTY   | NUMBER(12,3)        | No       |              | 29        | Piece QTY(Result.) / バラ実績数                  |
| SPPD_RTPC_QTY  | NUMBER(12,3)        | No       |              | 30        | Total Piece QTY(Result.) / 総バラ実績数           |
| SPPD_PIK1      | NVARCHAR2(30 CHAR)  | Yes      |              | 31        | PICKING KEY1 / ピッキングキー1                     |
| SPPD_PIK2      | NVARCHAR2(30 CHAR)  | Yes      |              | 32        | PICKING KEY2 / ピッキングキー2                     |
| SPPD_PIK3      | NVARCHAR2(30 CHAR)  | Yes      |              | 33        | PICKING KEY3 / ピッキングキー3                     |
| SPPD_PIK4      | NVARCHAR2(30 CHAR)  | Yes      |              | 34        | PICKING KEY4 / ピッキングキー4                     |
| SPPD_PIK5      | NVARCHAR2(30 CHAR)  | Yes      |              | 35        | PICKING KEY5 / ピッキングキー5                     |
| SPPD_PIK6      | NVARCHAR2(30 CHAR)  | Yes      |              | 36        | PICKING KEY6 / ピッキングキー6                     |
| SPPD_PIK7      | NVARCHAR2(30 CHAR)  | Yes      |              | 37        | PICKING KEY7 / ピッキングキー7                     |
| SPPD_AV_NUM    | NVARCHAR2(6 CHAR)   | Yes      |              | 38        | Arrival Number / 入荷番号                       |
| SPPD_AVLN_NUM  | NUMBER(4,0)         | Yes      |              | 39        | Arrival Line No / 入荷ラインNo                   |
| SPPD_AVSQ_NUM  | NUMBER(4,0)         | Yes      |              | 40        | Arrival Seq No / 入荷SEQNo                    |
| SPPD_RBL_QTY   | NUMBER(9,0)         | No       | '0'          | 41        | Ball QTY(Result.) / ボール実績数                  |
| SPPD_CFG1_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 42        | Customized Flag 01 / 顧客専用フラグ01              |
| SPPD_CFG2_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 43        | Customized Flag 02 / 顧客専用フラグ02              |
| SPPD_CFG3_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 44        | Customized Flag 03 / 顧客専用フラグ03              |
| SPPD_CFG4_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 45        | Customized Flag 04 / 顧客専用フラグ04              |
| SPPD_CFG5_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 46        | Customized Flag 05 / 顧客専用フラグ05              |
| SPPD_CFG6_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 47        | Customized Flag 06 / 顧客専用フラグ06              |
| SPPD_CFG7_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 48        | Customized Flag 07 / 顧客専用フラグ07              |
| SPPD_CFG8_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 49        | Customized Flag 08 / 顧客専用フラグ08              |
| SPPD_CFG9_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 50        | Customized Flag 09 / 顧客専用フラグ09              |
| SPPD_CFG10_FLG | CHAR(1 BYTE)        | No       | 'N'          | 51        | Customized Flag 10 / 顧客専用フラグ10              |
| SPPD_CFG11_FLG | CHAR(1 BYTE)        | No       | 'N'          | 52        | Customized Flag 11 / 顧客専用フラグ11              |
| SPPD_CFG12_FLG | CHAR(1 BYTE)        | No       | 'N'          | 53        | Customized Flag 12 / 顧客専用フラグ12              |
| SPPD_CFG13_FLG | CHAR(1 BYTE)        | No       | 'N'          | 54        | Customized Flag 13 / 顧客専用フラグ13              |
| SPPD_CFG14_FLG | CHAR(1 BYTE)        | No       | 'N'          | 55        | Customized Flag 14 / 顧客専用フラグ14              |
| SPPD_CFG15_FLG | CHAR(1 BYTE)        | No       | 'N'          | 56        | Customized Flag 15 / 顧客専用フラグ15              |
| SPPD_CNI1_NUM  | NUMBER(9,0)         | Yes      |              | 57        | Customized Number Item 01 / 顧客専用数字項目01      |
| SPPD_CNI2_NUM  | NUMBER(9,0)         | Yes      |              | 58        | Customized Number Item 02 / 顧客専用数字項目02      |
| SPPD_CNI3_NUM  | NUMBER(9,0)         | Yes      |              | 59        | Customized Number Item 03 / 顧客専用数字項目03      |
| SPPD_CNI4_NUM  | NUMBER(9,0)         | Yes      |              | 60        | Customized Number Item 04 / 顧客専用数字項目04      |
| SPPD_CNI5_NUM  | NUMBER(9,0)         | Yes      |              | 61        | Customized Number Item 05 / 顧客専用数字項目05      |
| SPPD_CNI6_NUM  | NUMBER(9,0)         | Yes      |              | 62        | Customized Number Item 06 / 顧客専用数字項目06      |
| SPPD_CNI7_NUM  | NUMBER(9,0)         | Yes      |              | 63        | Customized Number Item 07 / 顧客専用数字項目07      |
| SPPD_CNI8_NUM  | NUMBER(9,0)         | Yes      |              | 64        | Customized Number Item 08 / 顧客専用数字項目08      |
| SPPD_CNI9_NUM  | NUMBER(9,0)         | Yes      |              | 65        | Customized Number Item 09 / 顧客専用数字項目09      |
| SPPD_CNI10_NUM | NUMBER(14,4)        | Yes      |              | 66        | Customized Number Item 10 / 顧客専用数字項目10      |
| SPPD_CNI11_NUM | NUMBER(14,4)        | Yes      |              | 67        | Customized Number Item 11 / 顧客専用数字項目11      |
| SPPD_CNI12_NUM | NUMBER(14,4)        | Yes      |              | 68        | Customized Number Item 12 / 顧客専用数字項目12      |
| SPPD_CNI13_NUM | NUMBER(14,4)        | Yes      |              | 69        | Customized Number Item 13 / 顧客専用数字項目13      |
| SPPD_CNI14_NUM | NUMBER(14,4)        | Yes      |              | 70        | Customized Number Item 14 / 顧客専用数字項目14      |
| SPPD_CNI15_NUM | NUMBER(14,4)        | Yes      |              | 71        | Customized Number Item 15 / 顧客専用数字項目15      |
| SPPD_CIT1      | NVARCHAR2(100 CHAR) | Yes      |              | 72        | Customized Item 01 / 顧客専用項目01               |
| SPPD_CIT2      | NVARCHAR2(100 CHAR) | Yes      |              | 73        | Customized Item 02 / 顧客専用項目02               |
| SPPD_CIT3      | NVARCHAR2(100 CHAR) | Yes      |              | 74        | Customized Item 03 / 顧客専用項目03               |
| SPPD_CIT4      | NVARCHAR2(100 CHAR) | Yes      |              | 75        | Customized Item 04 / 顧客専用項目04               |
| SPPD_CIT5      | NVARCHAR2(100 CHAR) | Yes      |              | 76        | Customized Item 05 / 顧客専用項目05               |
| SPPD_CIT6      | NVARCHAR2(100 CHAR) | Yes      |              | 77        | Customized Item 06 / 顧客専用項目06               |
| SPPD_CIT7      | NVARCHAR2(100 CHAR) | Yes      |              | 78        | Customized Item 07 / 顧客専用項目07               |
| SPPD_CIT8      | NVARCHAR2(100 CHAR) | Yes      |              | 79        | Customized Item 08 / 顧客専用項目08               |
| SPPD_CIT9      | NVARCHAR2(100 CHAR) | Yes      |              | 80        | Customized Item 09 / 顧客専用項目09               |
| SPPD_CIT10     | NVARCHAR2(100 CHAR) | Yes      |              | 81        | Customized Item 10 / 顧客専用項目10               |
| SPPD_CIT11     | NVARCHAR2(100 CHAR) | Yes      |              | 82        | Customized Item 11 / 顧客専用項目11               |
| SPPD_CIT12     | NVARCHAR2(100 CHAR) | Yes      |              | 83        | Customized Item 12 / 顧客専用項目12               |
| SPPD_CIT13     | NVARCHAR2(100 CHAR) | Yes      |              | 84        | Customized Item 13 / 顧客専用項目13               |
| SPPD_CIT14     | NVARCHAR2(100 CHAR) | Yes      |              | 85        | Customized Item 14 / 顧客専用項目14               |
| SPPD_CIT15     | NVARCHAR2(100 CHAR) | Yes      |              | 86        | Customized Item 15 / 顧客専用項目15               |
