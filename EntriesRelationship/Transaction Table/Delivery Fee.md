# GWH_TJ_DVF
#Entity #Standard #OUTBOUND

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
| 18 | DVF_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | DVF_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | DVF_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | DVF_DV_NUM | CHAR(14) | N | Delivery Control Number / 配送管理番号 |
| 22 | DVF_DVLN_NUM | NUMBER(3) | N | Delivery Control Line Number / 配送管理行番号 |
| 23 | DVF_CALC_DATE | TIMESTAMP(6) | N | Delivery Fee Calculation Date / 運賃計算指示日時 |
| 24 | DVF_ADJ_AMN | NUMBER(9) | Y | Adjustment Amount / 調整額 |
| 25 | DVF_TAX_AMN | NUMBER(9) | Y | Sales Tax / 消費税 |
| 26 | DVF_TOLL_AMN | NUMBER(9) | Y | Toll Collection / 収受金 |
| 27 | DVF_DP_AMN | NUMBER(9) | Y | Delivery Provider Fee / 宅配便運賃 |
| 28 | DVF_DPRT_AMN | NUMBER(9) | Y | Delivery Provider(Return) Fee / 宅配便運賃（復路） |
| 29 | DVF_DPEX_AMN | NUMBER(9) | Y | Delivery Provider Extra Fee / 宅配便割増料金 |
| 30 | DVF_DPDS_AMN | NUMBER(9) | Y | Delivery Provider Discount Fee / 宅配便割引料金 |
| 31 | DVF_DPCC_AMN | NUMBER(9) | Y | Delivery Provider Cash On Delivery Commission / 宅配便代引手数料 |
| 32 | DVF_DPAC_AMN | NUMBER(9) | Y | Delivery Provider Airport Commission / 宅配便空港手数料 |
| 33 | DVF_DPMT_AMN | NUMBER(9) | Y | Delivery Provider Material Fee / 宅配便資材料金 |
| 34 | DVF_DPOT_AMN | NUMBER(9) | Y | Delivery Provider Other Fees / 宅配便諸料金 |
| 35 | DVF_RTIN_AMN | NUMBER(9) | Y | Route Insurance Fee / 路線便保険料 |
| 36 | DVF_RTAP_AMN | NUMBER(9) | Y | Route Advance Payment / 路線便（荷掛）立替金 |
| 37 | DVF_RT_AMN | NUMBER(9) | Y | Route Fare / 路線便運賃 |
| 38 | DVF_RTDS_AMN | NUMBER(9) | Y | Route Discount Fee / 路線便割引料金 |
| 39 | DVF_RTCL_AMN | NUMBER(9) | Y | Route Collect Fee / 路線便集荷料 |
| 40 | DVF_RTDL_AMN | NUMBER(9) | Y | Route Delivery Fee / 路線便配達料 |
| 41 | DVF_RTTR_AMN | NUMBER(9) | Y | Route Transfer Fee / 路線便移送料 |
| 42 | DVF_RTEX_AMN | NUMBER(9) | Y | Route District Extra Fee / 路線便地区割増料 |
| 43 | DVF_RTWE_AMN | NUMBER(9) | Y | Route Winter Extra Fee / 路線便冬季割増料 |
| 44 | DVF_RTRL_AMN | NUMBER(9) | Y | Route Relay Fee / 路線便連絡中継料 |
| 45 | DVF_RTUS_AMN | NUMBER(9) | Y | Route Usage Fee / 路線便航路利用料 |
| 46 | DVF_RTFS_AMN | NUMBER(9) | Y | Route Fuel Surcharge / 路線便燃料サーチャージ |
| 47 | DVF_RTOT_AMN | NUMBER(9) | Y | Route Other Fees / 路線便その他料 |
| 48 | DVF_RTSR_AMN | NUMBER(9) | Y | Route Storage Redelivery Fee / 路線便保管料・再配達料 |
| 49 | DVF_RTRI_AMN | NUMBER(9) | Y | Route Remote Island Fee / 路線便離島料 |
| 50 | DVF_RTAD_AMN | NUMBER(9) | Y | Route Actual Cost Discount Fee / 路線便実費割引 |
| 51 | DVF_RTID_AMN | NUMBER(9) | Y | Route Inside Delivery Fee / 路線便館内配送費 |
| 52 | DVF_RTOS_AMN | NUMBER(9) | Y | Route Other Actual Cost Special Car / 路線便その他実費（特殊車両） |
| 53 | DVF_RTOC_AMN | NUMBER(9) | Y | Route Other Actual Cost Charter / 路線便その他実費（チャーター） |
| 54 | DVF_RTOH_AMN | NUMBER(9) | Y | Route Other Actual Cost Helper / 路線便その他実費（助手） |
| 55 | DVF_RTOA_AMN | NUMBER(9) | Y | Route Other Actual Cost Collection / 路線便その他実費（専伝回収） |
| 56 | DVF_RTOP_AMN | NUMBER(9) | Y | Route Other Actual Cost Packing / 路線便その他実費（梱包費） |
| 57 | DVF_SLDT_WAY | NUMBER(7) | Y | Sales Distance Way / 営業距離 |
| 58 | DVF_DVDT_WAY | NUMBER(7) | Y | Delivery Distance Way / 配達距離 |
| 59 | DVF_CLDT_WAY | NUMBER(7) | Y | Collect Distance Way / 集荷距離 |
