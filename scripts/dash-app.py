import dash
from dash import dcc, html, Input, Output
import pandas as pd
import plotly.express as px
import plotly.graph_objects as go

# Load data
df = pd.read_csv("../output/greeks_surface.csv")

# App init
app = dash.Dash(__name__, suppress_callback_exceptions=True)

# Dropdown options
greek_options = ['Delta', 'Vega', 'Gamma', 'Theta']

# Layout
app.layout = html.Div([
    html.H1("Option Surface Visualizer"),

    dcc.Checklist(
        id='view-selector',
        options=[
            {'label': 'Smile Viewer', 'value': 'SMILE'},
            {'label': '3D Vol Surface', 'value': 'VOL3D'},
            {'label': 'Greeks Heatmap', 'value': 'GREEK'}
        ],
        value=['SMILE', 'VOL3D', 'GREEK'],
        labelStyle={'display': 'block'}
    ),

    html.Div(id='controls'),
    html.Div(id='graphs')
])

# Dynamic controls
@app.callback(
    Output('controls', 'children'),
    Input('view-selector', 'value')
)
def render_controls(views):
    controls = []
    if 'SMILE' in views:
        controls.append(html.Label("Maturity:"))
        controls.append(dcc.Slider(
            id='maturity-slider',
            min=df['Maturity'].min(),
            max=df['Maturity'].max(),
            step=0.1,
            marks={round(m,2): str(round(m,2)) for m in sorted(df['Maturity'].unique())},
            value=df['Maturity'].min()
        ))
    if 'GREEK' in views:
        controls.append(html.Label("Greek:"))
        controls.append(dcc.Dropdown(
            id='greek-dropdown',
            options=[{'label': g, 'value': g} for g in greek_options],
            value='Delta'
        ))
    return controls

# Graph renderer
@app.callback(
    Output('graphs', 'children'),
    [Input('view-selector', 'value'),
     Input('maturity-slider', 'value'),
     Input('greek-dropdown', 'value')],
     prevent_initial_call=True)
    
def render_graphs(views, selected_maturity, selected_greek):
    graphs = []

    if 'SMILE' in views:
        filtered = df[(df['Maturity'] >= selected_maturity - 1e-6) & 
              (df['Maturity'] <= selected_maturity + 1e-6)]
        filtered = filtered.drop_duplicates(subset=['Strike', 'Maturity'])
        filtered = filtered.sort_values(by='Strike')

        fig = px.line(filtered, x='Strike', y='Delta', title=f"Smile (Delta) at T={selected_maturity}")
        graphs.append(dcc.Graph(figure=fig))

    if 'VOL3D' in views:
        fig = go.Figure(data=[go.Surface(
            x=df['Strike'].unique(),
            y=df['Maturity'].unique(),
            z=df.pivot(index='Maturity', columns='Strike', values='Delta').values,
            colorscale='Viridis')])
        fig.update_layout(title="Delta Surface", scene=dict(
            xaxis_title='Strike', yaxis_title='Maturity', zaxis_title='Delta'))
        graphs.append(dcc.Graph(figure=fig))

    if 'GREEK' in views:
        pivot = df.pivot(index='Maturity', columns='Strike', values=selected_greek)
        fig = px.imshow(pivot.values,
                        labels=dict(x="Strike", y="Maturity", color=selected_greek),
                        x=pivot.columns,
                        y=pivot.index,
                        aspect="auto",
                        title=f"{selected_greek} Heatmap")
        graphs.append(dcc.Graph(figure=fig))

    return graphs

# Run the app
if __name__ == '__main__':
    app.run(debug=True)
