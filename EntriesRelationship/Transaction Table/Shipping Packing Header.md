# GWH_TJ_SP_PH
#Entity #Standard #OUTBOUND 

| COLUMN_NAME        | DATA_TYPE           | NULLABLE | DATA_DEFAULT | COLUMN_ID | COMMENTS                                      |
|--------------------|---------------------|----------|--------------|-----------|-----------------------------------------------|
| DEL_FLG            | CHAR(1 BYTE)        | No       |              | 1         | Delete Flag / 論理削除フラグ                         |
| CRT_YMD            | CHAR(8 BYTE)        | No       |              | 2         | Create Date (UTC) / 作成日(標準時)                  |
| CRT_TIM            | CHAR(6 BYTE)        | No       |              | 3         | Create HMS (UTC) / 作成時分秒(標準時)                 |
| CRT_TMID           | NVARCHAR2(100 CHAR) | No       |              | 4         | Create Term Id / 作成端末ID                       |
| CRT_USER           | NVARCHAR2(50 CHAR)  | No       |              | 5         | Create User Id / 作成ユーザID                      |
| CRT_PGM            | NVARCHAR2(30 CHAR)  | No       |              | 6         | Create Program Id / 作成プログラムID                 |
| CRT_TM_ZONE        | NVARCHAR2(3 CHAR)   | No       |              | 7         | Create User Time Zone / 作成ユーザタイムゾーン           |
| CRT_YMDHMS         | TIMESTAMP(6)        | No       |              | 8         | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時）      |
| CRT_L_YMDHMS       | TIMESTAMP(6)        | No       |              | 9         | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル）   |
| UPD_YMD            | CHAR(8 BYTE)        | No       |              | 10        | Update Date (UTC) / 更新日(標準時)                  |
| UPD_TIM            | CHAR(6 BYTE)        | No       |              | 11        | Update HMS (UTC) / 更新時分秒(標準時)                 |
| UPD_TMID           | NVARCHAR2(100 CHAR) | No       |              | 12        | Update Terminal ID / 更新端末ID                   |
| UPD_USER           | NVARCHAR2(50 CHAR)  | No       |              | 13        | Update User ID / 更新ユーザID                      |
| UPD_PGM            | NVARCHAR2(30 CHAR)  | No       |              | 14        | Update Program ID / 更新プログラムID                 |
| UPD_TM_ZONE        | NVARCHAR2(3 CHAR)   | No       |              | 15        | Update User Time Zone / 更新ユーザタイムゾーン           |
| UPD_YMDHMS         | TIMESTAMP(6)        | No       |              | 16        | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時）      |
| UPD_L_YMDHMS       | TIMESTAMP(6)        | No       |              | 17        | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル）   |
| SPPH_CPNY_COD      | NVARCHAR2(6 CHAR)   | No       |              | 18        | Company Code / カンパニーコード                       |
| SPPH_WHS_COD       | NVARCHAR2(4 CHAR)   | No       |              | 19        | Warehouse Code / 倉庫コード                        |
| SPPH_CUST_COD      | NVARCHAR2(13 CHAR)  | No       |              | 20        | Customer Code / 顧客コード                         |
| SPPH_PC_NUM        | NVARCHAR2(6 CHAR)   | No       |              | 21        | Packing Number / 梱包番号                         |
| SPPH_PCLN_NUM      | NUMBER(4,0)         | No       |              | 22        | Packing Line Number / 梱包ラインNo                 |
| SPPH_PCCT_NUM      | NVARCHAR2(20 CHAR)  | Yes      |              | 23        | Packing Carton ID / カートンID                    |
| SPPH_PCCT_QTY      | NUMBER(6,0)         | No       |              | 24        | Packing Carton Quantity / カートン数               |
| SPPH_PCPL_QTY      | NUMBER(6,0)         | No       |              | 25        | Packing Palette Quantity / パレット数              |
| SPPH_GRS_WGT       | NUMBER(12,6)        | No       |              | 26        | Gross Weight / 実重量                            |
| SPPH_NET_WGT       | NUMBER(12,6)        | No       |              | 27        | Net Weight / 正味重量                             |
| SPPH_M3            | NUMBER(12,6)        | No       |              | 28        | Volume / 容積                                   |
| SPPH_LEN           | NUMBER(12,6)        | No       |              | 29        | Length / 長さ                                   |
| SPPH_WID           | NUMBER(12,6)        | No       |              | 30        | Width / 幅                                     |
| SPPH_HIG           | NUMBER(12,6)        | No       |              | 31        | Height / 高さ                                   |
| SPPH_SIZE_COD      | NVARCHAR2(5 CHAR)   | Yes      |              | 32        | Size Code / サイズコード                            |
| SPPH_MTRL_COD      | NVARCHAR2(3 CHAR)   | Yes      |              | 33        | Material Code / 資材コード                         |
| SPPH_TRSP_CNFM_COD | NVARCHAR2(5 CHAR)   | Yes      |              | 34        | Transport Type Code (Confirmation) / 輸送手段（実績） |
| SPPH_ROUT_CNFM_COD | NVARCHAR2(5 CHAR)   | Yes      |              | 35        | Route Code (Confirmation) / ルートコード（実績）        |
| SPPH_CAR_CNFM_NUM  | NVARCHAR2(12 CHAR)  | Yes      |              | 36        | Car Number (Confirmation) / 車番（実績）            |
| SPPH_SPND_FLG      | CHAR(1 BYTE)        | No       |              | 37        | Suspend Flag / 中断フラグ                          |
| SPPH_CSRL_NUM      | NVARCHAR2(28 CHAR)  | Yes      |              | 38        | Carton Serial Number / カートンシリアルNo             |
| SPPH_PLTH_NUM      | NVARCHAR2(13 CHAR)  | Yes      |              | 39        | Palletize Number / パレットNo                     |
| SPPH_CFG1_FLG      | CHAR(1 BYTE)        | No       | 'N'          | 40        | Customized Flag 01 / 顧客専用フラグ01                |
| SPPH_CFG2_FLG      | CHAR(1 BYTE)        | No       | 'N'          | 41        | Customized Flag 02 / 顧客専用フラグ02                |
| SPPH_CFG3_FLG      | CHAR(1 BYTE)        | No       | 'N'          | 42        | Customized Flag 03 / 顧客専用フラグ03                |
| SPPH_CFG4_FLG      | CHAR(1 BYTE)        | No       | 'N'          | 43        | Customized Flag 04 / 顧客専用フラグ04                |
| SPPH_CFG5_FLG      | CHAR(1 BYTE)        | No       | 'N'          | 44        | Customized Flag 05 / 顧客専用フラグ05                |
| SPPH_CFG6_FLG      | CHAR(1 BYTE)        | No       | 'N'          | 45        | Customized Flag 06 / 顧客専用フラグ06                |
| SPPH_CFG7_FLG      | CHAR(1 BYTE)        | No       | 'N'          | 46        | Customized Flag 07 / 顧客専用フラグ07                |
| SPPH_CFG8_FLG      | CHAR(1 BYTE)        | No       | 'N'          | 47        | Customized Flag 08 / 顧客専用フラグ08                |
| SPPH_CFG9_FLG      | CHAR(1 BYTE)        | No       | 'N'          | 48        | Customized Flag 09 / 顧客専用フラグ09                |
| SPPH_CFG10_FLG     | CHAR(1 BYTE)        | No       | 'N'          | 49        | Customized Flag 10 / 顧客専用フラグ10                |
| SPPH_CFG11_FLG     | CHAR(1 BYTE)        | No       | 'N'          | 50        | Customized Flag 11 / 顧客専用フラグ11                |
| SPPH_CFG12_FLG     | CHAR(1 BYTE)        | No       | 'N'          | 51        | Customized Flag 12 / 顧客専用フラグ12                |
| SPPH_CFG13_FLG     | CHAR(1 BYTE)        | No       | 'N'          | 52        | Customized Flag 13 / 顧客専用フラグ13                |
| SPPH_CFG14_FLG     | CHAR(1 BYTE)        | No       | 'N'          | 53        | Customized Flag 14 / 顧客専用フラグ14                |
| SPPH_CFG15_FLG     | CHAR(1 BYTE)        | No       | 'N'          | 54        | Customized Flag 15 / 顧客専用フラグ15                |
| SPPH_CNI1_NUM      | NUMBER(9,0)         | Yes      |              | 55        | Customized Number Item 01 / 顧客専用数字項目01        |
| SPPH_CNI2_NUM      | NUMBER(9,0)         | Yes      |              | 56        | Customized Number Item 02 / 顧客専用数字項目02        |
| SPPH_CNI3_NUM      | NUMBER(9,0)         | Yes      |              | 57        | Customized Number Item 03 / 顧客専用数字項目03        |
| SPPH_CNI4_NUM      | NUMBER(9,0)         | Yes      |              | 58        | Customized Number Item 04 / 顧客専用数字項目04        |
| SPPH_CNI5_NUM      | NUMBER(9,0)         | Yes      |              | 59        | Customized Number Item 05 / 顧客専用数字項目05        |
| SPPH_CNI6_NUM      | NUMBER(9,0)         | Yes      |              | 60        | Customized Number Item 06 / 顧客専用数字項目06        |
| SPPH_CNI7_NUM      | NUMBER(9,0)         | Yes      |              | 61        | Customized Number Item 07 / 顧客専用数字項目07        |
| SPPH_CNI8_NUM      | NUMBER(9,0)         | Yes      |              | 62        | Customized Number Item 08 / 顧客専用数字項目08        |
| SPPH_CNI9_NUM      | NUMBER(9,0)         | Yes      |              | 63        | Customized Number Item 09 / 顧客専用数字項目09        |
| SPPH_CNI10_NUM     | NUMBER(14,4)        | Yes      |              | 64        | Customized Number Item 10 / 顧客専用数字項目10        |
| SPPH_CNI11_NUM     | NUMBER(14,4)        | Yes      |              | 65        | Customized Number Item 11 / 顧客専用数字項目11        |
| SPPH_CNI12_NUM     | NUMBER(14,4)        | Yes      |              | 66        | Customized Number Item 12 / 顧客専用数字項目12        |
| SPPH_CNI13_NUM     | NUMBER(14,4)        | Yes      |              | 67        | Customized Number Item 13 / 顧客専用数字項目13        |
| SPPH_CNI14_NUM     | NUMBER(14,4)        | Yes      |              | 68        | Customized Number Item 14 / 顧客専用数字項目14        |
| SPPH_CNI15_NUM     | NUMBER(14,4)        | Yes      |              | 69        | Customized Number Item 15 / 顧客専用数字項目15        |
| SPPH_CIT1          | NVARCHAR2(100 CHAR) | Yes      |              | 70        | Customized Item 01 / 顧客専用項目01                 |
| SPPH_CIT2          | NVARCHAR2(100 CHAR) | Yes      |              | 71        | Customized Item 02 / 顧客専用項目02                 |
| SPPH_CIT3          | NVARCHAR2(100 CHAR) | Yes      |              | 72        | Customized Item 03 / 顧客専用項目03                 |
| SPPH_CIT4          | NVARCHAR2(100 CHAR) | Yes      |              | 73        | Customized Item 04 / 顧客専用項目04                 |
| SPPH_CIT5          | NVARCHAR2(100 CHAR) | Yes      |              | 74        | Customized Item 05 / 顧客専用項目05                 |
| SPPH_CIT6          | NVARCHAR2(100 CHAR) | Yes      |              | 75        | Customized Item 06 / 顧客専用項目06                 |
| SPPH_CIT7          | NVARCHAR2(100 CHAR) | Yes      |              | 76        | Customized Item 07 / 顧客専用項目07                 |
| SPPH_CIT8          | NVARCHAR2(100 CHAR) | Yes      |              | 77        | Customized Item 08 / 顧客専用項目08                 |
| SPPH_CIT9          | NVARCHAR2(100 CHAR) | Yes      |              | 78        | Customized Item 09 / 顧客専用項目09                 |
| SPPH_CIT10         | NVARCHAR2(100 CHAR) | Yes      |              | 79        | Customized Item 10 / 顧客専用項目10                 |
| SPPH_CIT11         | NVARCHAR2(100 CHAR) | Yes      |              | 80        | Customized Item 11 / 顧客専用項目11                 |
| SPPH_CIT12         | NVARCHAR2(100 CHAR) | Yes      |              | 81        | Customized Item 12 / 顧客専用項目12                 |
| SPPH_CIT13         | NVARCHAR2(100 CHAR) | Yes      |              | 82        | Customized Item 13 / 顧客専用項目13                 |
| SPPH_CIT14         | NVARCHAR2(100 CHAR) | Yes      |              | 83        | Customized Item 14 / 顧客専用項目14                 |
| SPPH_CIT15         | NVARCHAR2(100 CHAR) | Yes      |              | 84        | Customized Item 15 / 顧客専用項目15                 |
