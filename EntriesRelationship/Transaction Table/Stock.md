# GWH_TJ_ST
#Entity #Standard #INBOUND #LOCATION

| COLUMN_NAME  | DATA_TYPE           | NULLABLE | DATA_DEFAULT | COLUMN_ID | COMMENTS                                             |
|--------------|---------------------|----------|--------------|-----------|------------------------------------------------------|
| DEL_FLG      | CHAR(1 BYTE)        | No       |              | 1         | Delete Flag / 論理削除フラグ                                |
| CRT_YMD      | CHAR(8 BYTE)        | No       |              | 2         | Create Date (UTC) / 作成日(標準時)                         |
| CRT_TIM      | CHAR(6 BYTE)        | No       |              | 3         | Create HMS (UTC) / 作成時分秒(標準時)                        |
| CRT_TMID     | NVARCHAR2(100 CHAR) | No       |              | 4         | Create Term Id / 作成端末ID                              |
| CRT_USER     | NVARCHAR2(50 CHAR)  | No       |              | 5         | Create User Id / 作成ユーザID                             |
| CRT_PGM      | NVARCHAR2(30 CHAR)  | No       |              | 6         | Create Program Id / 作成プログラムID                        |
| CRT_TM_ZONE  | NVARCHAR2(3 CHAR)   | No       |              | 7         | Create User Time Zone / 作成ユーザタイムゾーン                  |
| CRT_YMDHMS   | TIMESTAMP(6)        | No       |              | 8         | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時）             |
| CRT_L_YMDHMS | TIMESTAMP(6)        | No       |              | 9         | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル）          |
| UPD_YMD      | CHAR(8 BYTE)        | No       |              | 10        | Update Date (UTC) / 更新日(標準時)                         |
| UPD_TIM      | CHAR(6 BYTE)        | No       |              | 11        | Update HMS (UTC) / 更新時分秒(標準時)                        |
| UPD_TMID     | NVARCHAR2(100 CHAR) | No       |              | 12        | Update Terminal ID / 更新端末ID                          |
| UPD_USER     | NVARCHAR2(50 CHAR)  | No       |              | 13        | Update User ID / 更新ユーザID                             |
| UPD_PGM      | NVARCHAR2(30 CHAR)  | No       |              | 14        | Update Program ID / 更新プログラムID                        |
| UPD_TM_ZONE  | NVARCHAR2(3 CHAR)   | No       |              | 15        | Update User Time Zone / 更新ユーザタイムゾーン                  |
| UPD_YMDHMS   | TIMESTAMP(6)        | No       |              | 16        | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時）             |
| UPD_L_YMDHMS | TIMESTAMP(6)        | No       |              | 17        | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル）          |
| ST_CPNY_COD  | NVARCHAR2(6 CHAR)   | No       |              | 18        | Company Code / カンパニーコード                              |
| ST_WHS_COD   | NVARCHAR2(4 CHAR)   | No       |              | 19        | Warehouse Code / 倉庫コード                               |
| ST_CUST_COD  | NVARCHAR2(13 CHAR)  | No       |              | 20        | Customer Code / 顧客コード                                |
| ST_AV_NUM    | NVARCHAR2(6 CHAR)   | No       |              | 21        | Arrival Number / 入荷番号                                |
| ST_AVLN_NUM  | NUMBER(4,0)         | No       |              | 22        | Arrival Line No / 入荷ラインNo                            |
| ST_AVSQ_NUM  | NUMBER(4,0)         | No       |              | 23        | Arrival Seq No / 入荷SEQNo                             |
| ST_ST_KND    | CHAR(1 BYTE)        | No       |              | 24        | Stock Control Kind / 在庫管理区分                          |
| ST_AV_YMD    | DATE                | No       |              | 25        | Arrival Date / 入庫日                                   |
| ST_PROD_COD  | NVARCHAR2(50 CHAR)  | No       |              | 26        | Product Code / 商品コード                                 |
| ST_ORGN_COD  | CHAR(2 BYTE)        | Yes      |              | 27        | Country Of Origin / 原産国                              |
| ST_PIK1      | NVARCHAR2(30 CHAR)  | Yes      |              | 28        | PICKING KEY for allocation1 / ピッキングキー1               |
| ST_PIK2      | NVARCHAR2(30 CHAR)  | Yes      |              | 29        | PICKING KEY for allocation2 / ピッキングキー2               |
| ST_PIK3      | NVARCHAR2(30 CHAR)  | Yes      |              | 30        | PICKING KEY for allocation3 / ピッキングキー3               |
| ST_PIK4      | NVARCHAR2(30 CHAR)  | Yes      |              | 31        | PICKING KEY for refarence1 / ピッキングキー4                |
| ST_PIK5      | NVARCHAR2(30 CHAR)  | Yes      |              | 32        | PICKING KEY for refarence2 / ピッキングキー5                |
| ST_PIK6      | NVARCHAR2(30 CHAR)  | Yes      |              | 33        | PICKING KEY for refarence3 / ピッキングキー6                |
| ST_PIK7      | NVARCHAR2(30 CHAR)  | Yes      |              | 34        | PICKING KEY for refarence4 / ピッキングキー7                |
| ST_AREA_COD  | CHAR(3 BYTE)        | No       |              | 35        | Area / エリア                                           |
| ST_RACK_COD  | NVARCHAR2(10 CHAR)  | Yes      |              | 36        | Rack / ラック                                           |
| ST_PSTN_COD  | NVARCHAR2(3 CHAR)   | Yes      |              | 37        | Position / ポジション                                     |
| ST_LVL_COD   | NVARCHAR2(2 CHAR)   | Yes      |              | 38        | Level / レベル                                          |
| ST_SBIV_COD  | NVARCHAR2(20 CHAR)  | No       |              | 39        | Sub Inventry / 等級コード                                 |
| ST_PYST_QTY  | NUMBER(12,3)        | Yes      |              | 40        | Actual Stock (PCS) / 実在庫数量                           |
| ST_AVST_QTY  | NUMBER(12,3)        | Yes      |              | 41        | Available Stock (PCS) / 引当可能数量                       |
| ST_ALST_QTY  | NUMBER(12,3)        | Yes      |              | 42        | Allocated Stock (PCS) / 引当済数量                        |
| ST_PPCS_QTY  | NUMBER(9,0)         | Yes      |              | 43        | Number Of Pieces Per Case / ケース入数                    |
| ST_ORGN_QTY  | NUMBER(12,3)        | Yes      |              | 44        | Original Qty / 元数量                                   |
| ST_PCUM_PCS  | NVARCHAR2(3 CHAR)   | Yes      |              | 45        | UOM (piece) / 単位(バラ数)                                |
| ST_PCUM_CS   | NVARCHAR2(3 CHAR)   | Yes      |              | 46        | UOM (case) / 単位(ケース数)                                |
| ST_PSSA_FLG  | CHAR(1 BYTE)        | No       |              | 47        | PSSA Kind(Perfect Shaped Stock Allocation) / 成体管理フラグ |
| ST_PSSA_QTY  | NUMBER(9,3)         | Yes      |              | 48        | PSSA Incoming QTY / 成体入荷数量                           |
| ST_BOND_FLG  | CHAR(1 BYTE)        | Yes      |              | 49        | Bond Flag / 保税フラグ                                    |
| ST_DMG_FLG   | CHAR(1 BYTE)        | No       |              | 50        | Damage/Hold Flag / ダメージ/ホールドフラグ                      |
| ST_LOCK_FLG  | CHAR(1 BYTE)        | No       |              | 51        | Lock Flag / ロックフラグ                                   |
| ST_PPB_QTY   | NUMBER(9,0)         | Yes      |              | 52        | Number Of Pieces Per Ball / ボール入数                    |
| ST_PCUM_BL   | NVARCHAR2(3 CHAR)   | Yes      |              | 53        | UOM (Ball)                                           |
